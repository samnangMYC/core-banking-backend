package com.trendy.cbs.controller;

import com.trendy.cbs.payload.dto.AdminStaffDTO;
import com.trendy.cbs.payload.request.AdminStaffReq;
import com.trendy.cbs.service.AdminStaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/staff")
@RequiredArgsConstructor
public class AdminStaffController {

    private final AdminStaffService adminStaffService;

    @PostMapping
    public ResponseEntity<AdminStaffDTO> createStaff(
            @Valid @RequestBody AdminStaffReq req
    ) {
        log.info("Admin creating staff: email={}, role={}", req.getEmail(), req.getSystemRole());

        AdminStaffDTO response = adminStaffService.createStaff(req);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
