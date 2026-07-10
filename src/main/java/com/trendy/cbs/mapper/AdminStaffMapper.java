package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.Staff;
import com.trendy.cbs.payload.dto.AdminStaffDTO;
import com.trendy.cbs.payload.request.AdminStaffUpdateReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminStaffMapper {

    @Mapping(target = "staffId", source = "id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "keycloakUserId", source = "user.authProviderUserId")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    AdminStaffDTO toDto(Staff staff);

    List<AdminStaffDTO> toListDto(List<Staff> staff);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "systemRole", ignore = true)
    void updateEntityFromRequest(AdminStaffUpdateReq request,@MappingTarget Staff entity);

}
