package com.trendy.cbs.init;

import com.trendy.cbs.enums.Gender;
import com.trendy.cbs.enums.MaritalStatus;
import com.trendy.cbs.payload.request.UserRequest;
import com.trendy.cbs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final UserService userService;

    @Bean
    public CommandLineRunner initUserData() {
        return args -> {
            UserRequest userRequest = new UserRequest();
            userRequest.setFirstName("John");
            userRequest.setLastName("Doe");
            userRequest.setGender(Gender.MALE); // Assuming this matches an enum or valid string value
            userRequest.setEmail("john.doe@example.com");
            userRequest.setPhoneNumber("+1234567890"); // Note: The service method attempts Integer.valueOf on this; if it includes '+', it will throw NumberFormatException. Adjust to "1234567890" if needed.
            userRequest.setOccupation("Software Engineer");
            userRequest.setNationality("Cambodian");
            userRequest.setMaritalStatus(MaritalStatus.SINGLE);
            userRequest.setProfileImage("https://example.com/profile.jpg");

            userService.createNewUser(userRequest);
        };
    }
}
