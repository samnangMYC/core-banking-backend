package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.BranchDTO;
import com.trendy.cbs.payload.request.BranchRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface BranchService {
    BranchDTO createBranch(BranchRequest request);

    List<BranchDTO> getAllBranches();

    BranchDTO getBranchById(Long id);

    BranchDTO updateBranch(Long id, @Valid BranchRequest request);
}
