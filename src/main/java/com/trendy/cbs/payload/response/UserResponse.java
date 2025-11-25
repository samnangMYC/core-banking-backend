package com.trendy.cbs.payload.response;


import com.trendy.cbs.payload.dto.UserDTO;

import java.util.List;

public class UserResponse {
    private List<UserDTO> contents;
    private Integer pageSize;
    private Integer sortBy;
    private Integer sortOrder;
}
