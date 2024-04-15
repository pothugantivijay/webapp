package cloud.assignment;

import cloud.assignment.model.TokenEntity;
import cloud.assignment.model.User;
import cloud.assignment.repository.TokenRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TokenRepository tokenRepository;

    private String baseUrl;

    @BeforeEach
    public void setup() {
        this.baseUrl = "http://localhost:" + port;
    }

    @Test
    @Order(1)
    public void healthCheckTest() {
        // Test 123
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/healthz", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    private ResponseEntity<User> authUser(User body, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders(){{
            String auth = "user@example.com" + ":" + "pass";
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
        HttpEntity<User> request = new HttpEntity<>(body, headers);
        ResponseEntity<User> response = restTemplate.exchange(baseUrl+"/v5/user/self", method, request, User.class);
        return response;
    }

    TokenEntity tokenValue(String username){
        TokenEntity token =  new TokenEntity();
        token.setVerified(true);
        token.setExptime(new Timestamp(System.currentTimeMillis() + 2 * 60 * 1000));
        token.setEmail(username);
        token.setLink("abcdef");

        return token;
    }

    @Test
    @Order(2)
    public void createUserTest() {
        User newUser = new User();
        newUser.setFirst_name("first");
        newUser.setLast_name("last");
        newUser.setPassword("pass");
        newUser.setUsername("user@example.com");
        ResponseEntity<User> response = restTemplate.postForEntity(baseUrl + "/v5/user", newUser, User.class);
        TokenEntity token =tokenValue(newUser.getUsername());
        tokenRepository.save(token);
        ResponseEntity<TokenEntity> tokenResponse = restTemplate.getForEntity(baseUrl+"/verify?token=abcdef", null);

        ResponseEntity<User> getresponse = authUser(null, HttpMethod.GET);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(getresponse.getBody().getUsername()).isEqualTo(newUser.getUsername());
    }

    @Test
    @Order(3)
    public void updateUserTest() {

        ResponseEntity<User> get_user = authUser(null, HttpMethod.GET);
        User newUser = new User();
        newUser.setFirst_name("first_update");
        get_user.getBody().setFirst_name("first_update");
        newUser.setLast_name("last_update");
        get_user.getBody().setLast_name("last_update");
        newUser.setPassword("pass");
        get_user.getBody().setPassword("pass");

        ResponseEntity<User> response = authUser(newUser, HttpMethod.PUT);

        ResponseEntity<User> get_response = authUser(null, HttpMethod.GET);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(get_response.getBody().getUsername()).isEqualTo(get_user.getBody().getUsername());
}
}
