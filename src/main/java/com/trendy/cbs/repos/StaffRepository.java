package com.trendy.cbs.repos;

import com.trendy.cbs.entity.Staff;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff,Long> {
    boolean existsByPhoneNumber(@NotBlank(message = "Phone number is required") String phoneNumber);


}
