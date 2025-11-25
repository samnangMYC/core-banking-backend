package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.User;
import com.trendy.cbs.entity.UserProfile;
import com.trendy.cbs.payload.dto.UserDTO;
import com.trendy.cbs.payload.dto.UserWithProfile;
import com.trendy.cbs.payload.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRequest request);

    // Map profile fields into UserProfile
    @Mapping(target = "profileId", ignore = true)
    @Mapping(target = "user", ignore = true)
    UserProfile toProfile(UserRequest request);

    // user and user profile to UserDTO
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "profile", target = "profile")
    @Mapping(source = "user.status", target = "status")
    @Mapping(source = "user.createdAt", target = "createdAt")
    @Mapping(source = "user.updatedAt", target = "updatedAt")
    UserDTO toDto(User user, UserProfile profile);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.status", target = "status")
    @Mapping(source = "user.createdAt", target = "createdAt")
    @Mapping(source = "user.updatedAt", target = "updatedAt")
    @Mapping(source = "profile", target = "profile")
    UserDTO toDto(UserWithProfile source);

    // convert List of UserProfile to UserDTO
    List<UserDTO> toDtoList(List<UserWithProfile> profiles);
}
