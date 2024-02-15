package cloud.assignment;

import cloud.assignment.model.User;
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

}
