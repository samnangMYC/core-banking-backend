package com.trendy.cbs.payload.dto;

import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.entity.CustomerProfile;
import lombok.Data;

@Data
public class CustomerWithProfile {
    private Customer customer;
    private CustomerProfile profile;
}
