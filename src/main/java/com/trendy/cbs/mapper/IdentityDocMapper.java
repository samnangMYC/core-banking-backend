package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.IdentityDoc;
import com.trendy.cbs.payload.dto.IdentityDocDTO;
import com.trendy.cbs.payload.request.IdentityDocRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IdentityDocMapper {

    @Mapping(target = "user", ignore = true)
    IdentityDoc toEntity(IdentityDocRequest request);

    IdentityDocDTO toDTO(IdentityDoc identityDoc);

    List<IdentityDocDTO> toDTO(List<IdentityDoc> identityDocs);

    void updateEntityFromRequest(IdentityDocRequest request, @MappingTarget IdentityDoc entity);
}
