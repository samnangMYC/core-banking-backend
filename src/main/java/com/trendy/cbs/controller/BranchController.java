package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.BranchDTO;
import com.trendy.cbs.payload.request.BranchRequest;
import com.trendy.cbs.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/branch")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<BranchDTO> createBranch(@Valid @RequestBody BranchRequest request) {
        log.info("Creating a new branch{}", request.toString());
        return ResponseEntity.ok(branchService.createBranch(request));
    }

    @GetMapping
    public ResponseEntity<List<BranchDTO>> findAllBranches() {
        log.info("Finding all branches");
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @GetMapping("{id}")
    public ResponseEntity<BranchDTO> findBranchById(@PathVariable String id) {
        log.info("Finding a branch by id: {}", id);
        return ResponseEntity.ok(branchService.getBranchById(Long.valueOf(id)));
    }

    @PutMapping("{id}")
    public ResponseEntity<BranchDTO> updateBranch(@PathVariable Long id,@Valid @RequestBody BranchRequest request) {
        log.info("Updating a branch: {}", request.toString());
        return ResponseEntity.ok(branchService.updateBranch(id,request));
    }
}
