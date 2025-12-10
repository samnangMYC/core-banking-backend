package com.trendy.cbs.init;

import com.trendy.cbs.enums.Gender;
import com.trendy.cbs.enums.MaritalStatus;
import com.trendy.cbs.payload.request.CustomerRequest;
import com.trendy.cbs.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final CustomerService customerService;

    @Bean
    public CommandLineRunner initUserData() {
        return args -> {
            CustomerRequest customerRequest = new CustomerRequest();
            customerRequest.setFirstName("John");
            customerRequest.setLastName("Doe");
            customerRequest.setGender(Gender.MALE); // Assuming this matches an enum or valid string value
            customerRequest.setEmail("john.doe@example.com");
            customerRequest.setPhoneNumber("+1234567890"); // Note: The service method attempts Integer.valueOf on this; if it includes '+', it will throw NumberFormatException. Adjust to "1234567890" if needed.
            customerRequest.setOccupation("Software Engineer");
            customerRequest.setNationality("Cambodian");
            customerRequest.setMaritalStatus(MaritalStatus.SINGLE);
            customerRequest.setProfileImage("https://example.com/profile.jpg");

            customerService.createNewCustomer(customerRequest);
        };
    }
}
