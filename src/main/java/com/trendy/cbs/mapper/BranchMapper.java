package com.trendy.cbs.mapper;

import com.trendy.cbs.entity.Branch;
import com.trendy.cbs.payload.dto.BranchDTO;
import com.trendy.cbs.payload.request.BranchRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    Branch toEntity(BranchRequest request);

    BranchDTO toDTO(Branch entity);

    List<BranchDTO> toDTOList(List<Branch> entity);

    void updateEntityFromRequest(BranchRequest request, @MappingTarget Branch branch);
}
