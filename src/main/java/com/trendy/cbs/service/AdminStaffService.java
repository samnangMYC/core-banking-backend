package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.AdminStaffDTO;
import com.trendy.cbs.payload.request.AdminStaffReq;
import com.trendy.cbs.payload.request.AdminStaffUpdateReq;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AdminStaffService {
    AdminStaffDTO createStaff(AdminStaffReq req);

    AdminStaffDTO updateStaff(Long id, AdminStaffUpdateReq req);

    List<AdminStaffDTO> getAllStaff();
}
