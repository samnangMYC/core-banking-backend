package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.entity.CustomerProfile;
import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.dto.CustomerSummaryDTO;
import com.trendy.cbs.payload.dto.CustomerWithProfile;
import com.trendy.cbs.payload.request.CustomerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    // CustomerRequest to Customer Entity
    Customer toCustomer(CustomerRequest request);

    // Map profile fields into CustomerProfile
    @Mapping(target = "profileId", ignore = true)
    @Mapping(target = "customer", ignore = true)
    CustomerProfile toProfile(CustomerRequest request);

    // customer and customer profile to CustomerDTO
    @Mapping(source = "customer.cusId", target = "cusId")
    @Mapping(source = "customer.status", target = "status")
    @Mapping(source = "customer.verification", target = "verification")
    @Mapping(source = "profile", target = "profile")
    @Mapping(source = "customer.createdAt", target = "createdAt")
    @Mapping(source = "customer.updatedAt", target = "updatedAt")
    CustomerDTO toDto(Customer customer, CustomerProfile profile);

    // convert CustomerWithProfile to CustomerDTO
    // usage at toDTOList for mapping
    @Mapping(source = "customer.cusId", target = "cusId")
    @Mapping(source = "customer.status", target = "status")
    @Mapping(source = "customer.verification", target = "verification")
    @Mapping(source = "profile", target = "profile")
    @Mapping(source = "customer.createdAt", target = "createdAt")
    @Mapping(source = "customer.updatedAt", target = "updatedAt")
    CustomerDTO toDto(CustomerWithProfile source);

    // convert List of CustomerProfile to CustomerDTO
    List<CustomerDTO> toDtoList(List<CustomerWithProfile> profiles);

    @Mapping(target = "fullName", expression = "java(buildFullName(customer))")
    @Mapping(target = "phoneNumber", expression = "java(mapPhoneNumber(customer))")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "verification", source = "verification")
    CustomerSummaryDTO toSummaryDTO(Customer customer);

    default String buildFullName(Customer customer) {
        if (customer == null || customer.getProfile() == null) {
            return null;
        }

        String firstName = customer.getProfile().getFirstName();
        String lastName  = customer.getProfile().getLastName();

        if (firstName == null && lastName == null) {
            return null;
        }

        return String.join(" ",
                firstName != null ? firstName : "",
                lastName != null ? lastName : ""
        ).trim();
    }

    default String mapPhoneNumber(Customer customer) {
        return customer.getProfile() != null ? String.valueOf(customer.getProfile().getPhoneNumber()) : null;
    }

}
