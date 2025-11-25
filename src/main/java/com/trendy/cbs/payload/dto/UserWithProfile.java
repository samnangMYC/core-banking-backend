package com.trendy.cbs.payload.dto;

import com.trendy.cbs.entity.User;
import com.trendy.cbs.entity.UserProfile;
import lombok.Data;

@Data
public class UserWithProfile {
    private User user;
    private UserProfile profile;
}
