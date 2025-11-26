package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.UserProfile;
import com.trendy.cbs.payload.dto.UserProfileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileDTO toDto(UserProfile profile);
}
