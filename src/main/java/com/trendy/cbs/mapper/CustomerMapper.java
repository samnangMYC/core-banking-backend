package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.entity.CustomerProfile;
import com.trendy.cbs.payload.dto.CustomerDTO;
import com.trendy.cbs.payload.dto.CustomerWithProfile;
import com.trendy.cbs.payload.request.CustomerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
    @Mapping(source = "profile", target = "profile")
    @Mapping(source = "customer.status", target = "status")
    @Mapping(source = "customer.createdAt", target = "createdAt")
    @Mapping(source = "customer.updatedAt", target = "updatedAt")
    CustomerDTO toDto(Customer customer, CustomerProfile profile);

    // convert CustomerWithProfile to CustomerDTO
    @Mapping(source = "customer.cusId", target = "cusId")
    @Mapping(source = "customer.status", target = "status")
    @Mapping(source = "customer.createdAt", target = "createdAt")
    @Mapping(source = "customer.updatedAt", target = "updatedAt")
    @Mapping(source = "profile", target = "profile")
    CustomerDTO toDto(CustomerWithProfile source);

    // convert List of CustomerProfile to CustomerDTO
    List<CustomerDTO> toDtoList(List<CustomerWithProfile> profiles);


}
