package com.trendy.cbs.payload.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private Long addressId;

    private String line1;

    private String line2;

    private String city;

    private String state;

    private String country;

}
