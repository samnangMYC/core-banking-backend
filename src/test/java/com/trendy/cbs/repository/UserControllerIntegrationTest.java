//package com.trendy.cbs.repository;
//
//import com.trendy.cbs.enums.UserStatus;
//import com.trendy.cbs.payload.dto.UserDTO;
//import com.trendy.cbs.payload.request.UserRequest;
//import net.bytebuddy.utility.dispatcher.JavaDispatcher;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.resttestclient.TestRestTemplate;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Testcontainers
//class UserControllerIntegrationTest {
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//    @JavaDispatcher.Container
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
//
//    // Assume dynamic properties for DB connection (e.g., via @DynamicPropertySource)
//
//    @Test
//    @DisplayName("End-to-End: Create and Get Users")
//    void createAndGetUsers_Integration_Success() {
//        // Given: Create user
//        UserRequest request = UserRequest.builder()
//                .firstName("Jane")
//                .lastName("Doe")
//                .email("jane.doe@example.com")
//                .phoneNumber("1234567890")
//                .build();
//        ResponseEntity<UserDTO> createResponse = restTemplate.postForEntity("/api/v1/users", request, UserDTO.class);
//
//        // Then: Create success
//        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
//        assertNotNull(createResponse.getBody());
//        assertEquals("Jane", createResponse.getBody().getProfile().getFirstName());
//        assertEquals(UserStatus.ACTIVE, createResponse.getBody().getStatus());
//
//        // When: Get all
//        ResponseEntity<UserDTO[]> getResponse = restTemplate.getForEntity("/api/v1/users", UserDTO[].class);
//
//        // Then: Get success
//        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
//        assertTrue(getResponse.getBody().length >= 1);
//        assertEquals("Jane", getResponse.getBody()[0].getProfile().getFirstName());
//    }
//}