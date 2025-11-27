package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.AddressDTO;
import com.trendy.cbs.payload.request.AddressRequest;
import com.trendy.cbs.payload.request.UpdateAddressRequest;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface AddressService {

    AddressDTO createUserAddress(@Valid AddressRequest request);


     List<AddressDTO> getAllUserAddress();

     AddressDTO updateUserAddress(Long id,@Valid UpdateAddressRequest request);

    @Nullable String deleteUserAddressById(Long id);

    @Nullable AddressDTO getUserAddressById(Long id);
}
