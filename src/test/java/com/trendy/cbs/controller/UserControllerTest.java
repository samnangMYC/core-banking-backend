//package com.trendy.cbs.controller;
//
//import com.trendy.cbs.enums.Gender;
//import com.trendy.cbs.enums.MaritalStatus;
//import com.trendy.cbs.enums.UserStatus;
//import com.trendy.cbs.exception.DuplicationResource;
//import com.trendy.cbs.payload.dto.UserDTO;
//import com.trendy.cbs.payload.dto.UserProfileDTO;
//import com.trendy.cbs.payload.request.UserRequest;
//import com.trendy.cbs.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import tools.jackson.databind.ObjectMapper;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(UserController.class)
//public class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private UserService userService; // Mock the service to isolate controller logic, but for full integration, you could remove this and use real service with embedded DB
//
//    private UserRequest validUserRequest;
//
//    @BeforeEach
//    public void setUp() {
//        validUserRequest = UserRequest.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .gender(Gender.MALE)
//                .email("john.doe@example.com")
//                .phoneNumber("1234567890") // Assuming numeric string without '+' to avoid NumberFormatException
//                .occupation("Software Engineer")
//                .nationality("Cambodian")
//                .maritalStatus(MaritalStatus.SINGLE)
//                .profileImage("https://example.com/profile.jpg")
//                .build();
//    }
//
//    @Test
//    public void testCreateUser_Success() throws Exception {
//        // Mock service response
//        UserDTO mockUserDTO = UserDTO.builder()
//                .userId(1L)
//                .status(UserStatus.ACTIVE)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .profile(UserProfileDTO.builder()
//                        .firstName("John")
//                        .lastName("Doe")
//                        .gender(Gender.MALE)
//                        .email("john.doe@example.com")
//                        .phoneNumber(1234567890)
//                        .occupation("Software Engineer")
//                        .nationality("Cambodian")
//                        .maritalStatus("SINGLE")
//                        .profileImage("https://example.com/profile.jpg")
//                        .build())
//                .build();
//
//        Mockito.when(userService.createNewUser(any(UserRequest.class))).thenReturn(mockUserDTO);
//
//        // Perform POST request
//        mockMvc.perform(MockMvcRequestBuilders.post("/your-endpoint") // Replace with actual endpoint path, e.g., "/users"
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(validUserRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userId").value(1L))
//                .andExpect(jsonPath("$.status").value("ACTIVE"))
//                .andExpect(jsonPath("$.profile.firstName").value("John"))
//                .andExpect(jsonPath("$.profile.lastName").value("Doe"))
//                .andExpect(jsonPath("$.profile.gender").value("MALE"))
//                .andExpect(jsonPath("$.profile.email").value("john.doe@example.com"))
//                .andExpect(jsonPath("$.profile.phoneNumber").value(1234567890))
//                .andExpect(jsonPath("$.profile.occupation").value("Software Engineer"))
//                .andExpect(jsonPath("$.profile.nationality").value("Cambodian"))
//                .andExpect(jsonPath("$.profile.maritalStatus").value("SINGLE"))
//                .andExpect(jsonPath("$.profile.profileImage").value("https://example.com/profile.jpg"));
//    }
//
//    @Test
//    public void testCreateUser_DuplicatePhoneNumber() throws Exception {
//        // Mock service to throw DuplicationResource exception
//        Mockito.when(userService.createNewUser(any(UserRequest.class)))
//                .thenThrow(new DuplicationResource("Phone number already exists"));
//
//        // Perform POST request
//        mockMvc.perform(MockMvcRequestBuilders.post("/your-endpoint") // Replace with actual endpoint path
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(validUserRequest)))
//                .andExpect(status().isConflict()) // Assuming DuplicationResource leads to 409 Conflict; adjust based on your exception handler
//                .andExpect(jsonPath("$.message").value("Phone number already exists")); // Adjust based on your error response format
//    }
//
//    @Test
//    public void testCreateUser_ValidationFailure_MissingGender() throws Exception {
//        UserRequest invalidRequest = UserRequest.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("john.doe@example.com")
//                .phoneNumber("1234567890")
//                .occupation("Software Engineer")
//                .nationality("Cambodian")
//                .maritalStatus(MaritalStatus.SINGLE)
//                .profileImage("https://example.com/profile.jpg")
//                .build(); // Missing gender
//
//        // Perform POST request
//        mockMvc.perform(MockMvcRequestBuilders.post("/your-endpoint") // Replace with actual endpoint path
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidRequest)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Gender is required")); // Adjust based on your validation error response
//    }
//
//    @Test
//    public void testCreateUser_ValidationFailure_MissingNationality() throws Exception {
//        UserRequest invalidRequest = UserRequest.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .gender(Gender.MALE)
//                .email("john.doe@example.com")
//                .phoneNumber("1234567890")
//                .occupation("Software Engineer")
//                .maritalStatus(MaritalStatus.SINGLE)
//                .profileImage("https://example.com/profile.jpg")
//                .build(); // Missing nationality
//
//        // Perform POST request
//        mockMvc.perform(MockMvcRequestBuilders.post("/your-endpoint") // Replace with actual endpoint path
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidRequest)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Nationality is required")); // Adjust based on your validation error response
//    }
//
//    @Test
//    public void testCreateUser_InvalidPhoneNumberFormat() throws Exception {
//        UserRequest invalidRequest = UserRequest.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .gender(Gender.MALE)
//                .email("john.doe@example.com")
//                .phoneNumber("+1234567890") // Invalid format with '+'
//                .occupation("Software Engineer")
//                .nationality("Cambodian")
//                .maritalStatus(MaritalStatus.SINGLE)
//                .profileImage("https://example.com/profile.jpg")
//                .build();
//
//        // Mock service to throw NumberFormatException (or handle it in your code)
//        Mockito.when(userService.createNewUser(any(UserRequest.class)))
//                .thenThrow(new NumberFormatException("For input string: \"+1234567890\""));
//
//        // Perform POST request
//        mockMvc.perform(MockMvcRequestBuilders.post("/your-endpoint") // Replace with actual endpoint path
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidRequest)))
//                .andExpect(status().isBadRequest()) // Or appropriate status based on exception handler
//                .andExpect(jsonPath("$.message").value("Invalid phone number format")); // Adjust based on your error response
//    }
//}
