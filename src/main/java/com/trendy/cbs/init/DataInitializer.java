package com.trendy.cbs.init;

import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.enums.*;
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
            CustomerRequest customerRequest = new CustomerRequest();
            customerRequest.setFirstName("John");
            customerRequest.setLastName("Doe");
            customerRequest.setGender(Gender.MALE);
            customerRequest.setEmail("john.doe@example.com");
            customerRequest.setPhoneNumber("1234567890");
            customerRequest.setOccupation("Software Engineer");
            customerRequest.setNationality("Cambodian");
            customerRequest.setMaritalStatus(MaritalStatus.SINGLE);
            customerRequest.setProfileImage("https://example.com/profile.jpg");

            customerService.createNewCustomer(customerRequest);

            CustomerVerificationReq request = new CustomerVerificationReq();
            request.setVerification(CustomerVerification.VERIFIED);
            customerService.verifyCustomer(1L,request);
        };
    }

    @Bean
    @Order(3)
    public CommandLineRunner initCurrencyData() {
        return args -> {
            CurrencyRequest currencyRequest = new CurrencyRequest();
            currencyRequest.setCode("USD");
            currencyRequest.setName("US Dollar");
            currencyRequest.setRate(BigDecimal.valueOf(1));

            currencyService.createCurrency(currencyRequest);
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
           branchRequest.setCity("USA");
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
            accountTypeRequest.setMaxTranBalanceDaily(BigDecimal.valueOf(100000));
            accountTypeRequest.setAtmLimitedTranDaily(BigDecimal.valueOf(1000));
            accountTypeRequest.setFeeYearly(BigDecimal.valueOf(10.0));

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
