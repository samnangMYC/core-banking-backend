package com.trendy.cbs.service;

import com.trendy.cbs.payload.dto.AdminStaffDTO;
import com.trendy.cbs.payload.request.AdminStaffReq;
import jakarta.validation.Valid;

public interface AdminStaffService {
    AdminStaffDTO createStaff(AdminStaffReq req);
}
