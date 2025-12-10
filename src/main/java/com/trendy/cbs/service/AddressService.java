package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.AddressDTO;
import com.trendy.cbs.payload.request.AddressRequest;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface AddressService {

    AddressDTO createAddress(Long userId,@Valid AddressRequest request);


     List<AddressDTO> getAllAddress();

     AddressDTO updateAddress(Long id,@Valid AddressRequest request);

     String deleteAddressById(Long id);

     AddressDTO getAddressById(Long id);
}
