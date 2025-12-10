package com.trendy.cbs.payload.response;


import com.trendy.cbs.payload.dto.CustomerDTO;

import java.util.List;

public class UserResponse {
    private List<CustomerDTO> contents;
    private Integer pageSize;
    private Integer sortBy;
    private Integer sortOrder;
}
