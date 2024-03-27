package cloud.assignment;

import cloud.assignment.model.TokenEntity;
import cloud.assignment.model.User;
import cloud.assignment.repository.TokenRepository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.UUID;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders createHeaders(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        return headers;
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/v1/user";
    }

    @Test
    @Order(1)
    public void testCreateAndGetUser() {
        // Create user
        User newUser = new User("testUser@gmail.com", "testPass", "Test", "User");
        HttpEntity<User> entity = new HttpEntity<>(newUser, null);
        ResponseEntity<Object> createResponse = restTemplate.exchange(baseUrl(), HttpMethod.POST, entity, Object.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        // Validate user exists
        HttpEntity<User> getEntity = new HttpEntity<>(null, createHeaders(newUser.getUsername(), newUser.getPassword()));
        ResponseEntity<User> getResponse = restTemplate.exchange(baseUrl() + "/self", HttpMethod.GET, getEntity, User.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("testUser@gmail.com", getResponse.getBody().getUsername());
    }

    @Test
    @Order(2)
    public void testUpdateAndGetUser() {

        // Update user
        User updatedUser = new User("testUser@gmail.com", "testPass", "NewTest", "User");
        HttpEntity<User> updateEntity = new HttpEntity<>(updatedUser, createHeaders(updatedUser.getUsername(), updatedUser.getPassword()));
        ResponseEntity<User> updateResponse = restTemplate.exchange(baseUrl() + "/self", HttpMethod.PUT, updateEntity, User.class);
        assertEquals(HttpStatus.NO_CONTENT, updateResponse.getStatusCode());

        // Validate user was updated
        HttpEntity<User> getEntity = new HttpEntity<>(null, createHeaders(updatedUser.getUsername(), updatedUser.getPassword()));
        ResponseEntity<User> getResponse = restTemplate.exchange(baseUrl() + "/self", HttpMethod.GET, getEntity, User.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("testUser@gmail.com", getResponse.getBody().getUsername());
    }


    public class VerificationControllerTest {

        @Autowired
        private TestRestTemplate restTemplate;

        @Autowired
        private TokenRepository tokenRepository;

        @Test
        @Order(3)
        public void testVerifyToken() {
            // Create a new token entity
            TokenEntity tokenEntity = new TokenEntity();
            String token = UUID.randomUUID().toString();
            tokenEntity.setToken(token);
            tokenEntity.setEmail("test@example.com");
            tokenEntity.setExpiration(new Date(System.currentTimeMillis() + 120000)); // 2 minutes from now
            tokenEntity.setVerified(true);
            tokenRepository.save(tokenEntity);

            // Call the verification endpoint
            ResponseEntity<String> response = restTemplate.getForEntity("/verify?token=" + token, String.class);

            // Check response status and content
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().contains("Verified successfully"));

            // Check if the token status is updated in the database
            TokenEntity updatedToken = tokenRepository.findById(token).orElse(null);
            assertNotNull(updatedToken);
            assertTrue(updatedToken.isVerified());
        }

        // Other tests...

}
}
