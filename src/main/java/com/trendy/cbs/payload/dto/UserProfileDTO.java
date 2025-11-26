package com.trendy.cbs.payload.dto;

import com.trendy.cbs.enums.Gender;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private Integer phoneNumber;
    private String occupation;
    private String nationality;
    private String maritalStatus;
    private String profileImage;
}
