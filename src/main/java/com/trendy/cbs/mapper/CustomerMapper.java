package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.payload.dto.CustomerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.email", target = "email")
    CustomerDTO toDto(Customer customer);

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.email", target = "email")
    List<CustomerDTO> toListDto(List<Customer> customers);

}
