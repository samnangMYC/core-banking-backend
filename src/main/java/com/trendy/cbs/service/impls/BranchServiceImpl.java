package com.trendy.cbs.service.impls;

import com.trendy.cbs.entity.Branch;
import com.trendy.cbs.enums.BranchStatus;
import com.trendy.cbs.exception.DuplicationResource;
import com.trendy.cbs.exception.ResourceNotFoundException;
import com.trendy.cbs.mapper.BranchMapper;
import com.trendy.cbs.payload.dto.BranchDTO;
import com.trendy.cbs.payload.request.BranchRequest;
import com.trendy.cbs.repos.BranchRepository;
import com.trendy.cbs.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Override
    public BranchDTO createBranch(BranchRequest request) {

        // Check for existing branch by name to prevent duplicates
        if(branchRepository.existsByBranchName(request.getBranchName())){
            throw new DuplicationResource("Branch with name '" + request.getBranchName() + "' already exists.");
        }

        // convert branch req to branch entity
        Branch branch = branchMapper.toEntity(request);
        branch.setBranchCode(generateBranchCode(branch.getBranchName()));
        branch.setOpenedDate(LocalDate.now());
        branch.setStatus(BranchStatus.ACTIVE);
        branch.setClosedDate(null);

        // return branch entity to dto
        return branchMapper.toDTO(branchRepository.save(branch));
    }

    @Override
    public List<BranchDTO> getAllBranches() {
        return branchMapper.toDTOList(branchRepository.findAll());
    }

    @Override
    public BranchDTO getBranchById(Long id) {
        return branchRepository.findById(id)
                .map(branchMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Branch",id));
    }

    @Override
    public BranchDTO updateBranch(Long id, BranchRequest request) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch",id));
        branchMapper.updateEntityFromRequest(request, branch);

        return branchMapper.toDTO(branchRepository.save(branch));
    }

    private String generateBranchCode(String branchName) {
        // Clean branch name for code (uppercase, no spaces/special chars)
        String prefix = branchName.toUpperCase().replaceAll("[^A-Z0-9]", "");

        // Get last code for this branch prefix
        String lastCode = branchRepository.findLastBranchCode(prefix);

        int nextNumber = 1;
        if (lastCode != null && lastCode.contains("-")) {
            // Extract numeric part and increment
            String numberPart = lastCode.substring(lastCode.lastIndexOf('-') + 1);
            nextNumber = Integer.parseInt(numberPart) + 1;
        }

        // Format code with leading zeros
        return String.format("%s-%04d", prefix, nextNumber);
    }

}
