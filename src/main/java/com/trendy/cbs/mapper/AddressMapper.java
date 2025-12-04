package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.Address;
import com.trendy.cbs.payload.dto.AddressDTO;
import com.trendy.cbs.payload.request.AddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(AddressRequest request);

    AddressDTO toDTO(Address address);

    List<AddressDTO> toDTOList(List<Address> address);

    void updateEntityFromRequest(AddressRequest request,@MappingTarget Address address);
}
