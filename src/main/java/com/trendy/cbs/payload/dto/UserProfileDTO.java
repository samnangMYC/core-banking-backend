package com.trendy.cbs.payload.dto;

import com.trendy.cbs.enums.Gender;

import lombok.Data;

@Data
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
