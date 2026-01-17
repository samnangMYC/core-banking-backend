package com.trendy.cbs.init;

import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.enums.*;
import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.request.*;
import com.trendy.cbs.service.*;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final CustomerService customerService;
    private final CurrencyService currencyService;
    private final BranchService branchService;
    private final AccountTypeService accountTypeService;
    private final AddressService addressService;


    @Bean
    @Order(2)
    public CommandLineRunner initCustomerData() {
        return args -> {
            // -----------------------------
            // 1️⃣ Create customer John Doe
            // -----------------------------
            CustomerRequest johnRequest = new CustomerRequest();
            johnRequest.setFirstName("John");
            johnRequest.setLastName("Doe");
            johnRequest.setGender(Gender.MALE);
            johnRequest.setEmail("john.doe@example.com");
            johnRequest.setPhoneNumber("1234567890");
            johnRequest.setOccupation("Software Engineer");
            johnRequest.setNationality("Cambodian");
            johnRequest.setMaritalStatus(MaritalStatus.SINGLE);
            johnRequest.setProfileImage("https://example.com/profile.jpg");

            // Create customer
            CustomerDTO johnResponse = customerService.createNewCustomer(johnRequest);

            // -----------------------------
            // 2️⃣ Verify the customer
            // -----------------------------
            CustomerVerificationReq verificationReq = new CustomerVerificationReq();
            verificationReq.setVerification(CustomerVerification.VERIFIED);

            // Use the ID returned from creation
            customerService.verifyCustomer(johnResponse.getCusId(), verificationReq);

            // -----------------------------
            // Optional: Add more customers
            // -----------------------------
            CustomerRequest janeRequest = new CustomerRequest();
            janeRequest.setFirstName("Jane");
            janeRequest.setLastName("Smith");
            janeRequest.setGender(Gender.FEMALE);
            janeRequest.setEmail("jane.smith@example.com");
            janeRequest.setPhoneNumber("0987654321");
            janeRequest.setOccupation("Teacher");
            janeRequest.setNationality("Cambodian");
            janeRequest.setMaritalStatus(MaritalStatus.MARRIED);
            janeRequest.setProfileImage("https://example.com/jane.jpg");

            CustomerDTO janeResponse = customerService.createNewCustomer(janeRequest);
            verificationReq.setVerification(CustomerVerification.VERIFIED);
            customerService.verifyCustomer(janeResponse.getCusId(), verificationReq);
        };
    }

    @Bean
    @Order(3)
    public CommandLineRunner initCurrencyData() {
        return args -> {
            // USD
            CurrencyRequest usdRequest = new CurrencyRequest();
            usdRequest.setCode("USD");
            usdRequest.setName("US Dollar");
            usdRequest.setSymbol("$");
            usdRequest.setDecimalPlaces(2);
            currencyService.createCurrency(usdRequest);

            // KHR
            CurrencyRequest khrRequest = new CurrencyRequest();
            khrRequest.setCode("KHR");
            khrRequest.setName("Khmer Riel");
            khrRequest.setSymbol("៛");
            khrRequest.setDecimalPlaces(0);
            currencyService.createCurrency(khrRequest);
        };
    }

    @Bean
    @Order(1)
    public CommandLineRunner initBrandData() {
        return args -> {
           BranchRequest branchRequest = new BranchRequest();
           branchRequest.setBranchName("Central Branch");
           branchRequest.setBranchType(BranchType.HEAD_OFFICE);
           branchRequest.setLine1("123 Main Street");
           branchRequest.setLine2("Suite 400");
           branchRequest.setCity("Metropolis");
           branchRequest.setState("New York");
           branchRequest.setCountry("USA");
           branchRequest.setPostalCode("12345");
           branchRequest.setPhoneNumber("1-212-555-1234");
           branchRequest.setEmail("central.branch@example.com");
           branchRequest.setLatitude(BigDecimal.valueOf(40.712776));
           branchRequest.setLongitude(BigDecimal.valueOf(-71.43232));
           branchRequest.setIfscCode("CENB0000123");
           branchRequest.setSwiftCode("CENSUS33");

           branchService.createBranch(branchRequest);
        };
    }
    @Bean
    @Order(4)
    public CommandLineRunner initAccountTypeData() {
        return args -> {
            AccountTypeRequest accountTypeRequest = new AccountTypeRequest();
            accountTypeRequest.setCode("SAV001");
            accountTypeRequest.setName("Savings Account");
            accountTypeRequest.setDescription("A basic savings account with interest.");
            accountTypeRequest.setInterestRate(BigDecimal.valueOf(0.05));
            accountTypeRequest.setPurposeType(PurposeType.SAVINGS);

            accountTypeService.createAccountType(accountTypeRequest);
        };
    }

    @Bean
    @Order(5)
    public CommandLineRunner initCustomerAddressData() {
        return args -> {

            Long customerId = 1L;
            AddressRequest addressRequest = new AddressRequest();
            addressRequest.setLine1("123 Main Street");
            addressRequest.setLine2("Suite 400");
            addressRequest.setCity("Metropolis");
            addressRequest.setState("New York");
            addressRequest.setCountry("USA");

            addressService.createAddress(customerId,addressRequest);

        };
    }

    @Bean
    @Order(6)
    public CommandLineRunner initIdentityDocData(IdentityDocService identityDocService) {
        return args -> {
            Long customerId = 1L;
            IdentityDocRequest identityDocRequest = new IdentityDocRequest();
            identityDocRequest.setIdentityType(IdentityType.NID);
            identityDocRequest.setNumber("AIE33244434551");
            identityDocRequest.setExpDate(LocalDate.ofEpochDay(2030-12-31));
            identityDocRequest.setDocImage("https://example.com/doc.jpg");

            identityDocService.createIdentityDoc(customerId,identityDocRequest);



        };
    }

}
