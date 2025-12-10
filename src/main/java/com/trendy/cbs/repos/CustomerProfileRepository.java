package com.trendy.cbs.repos;

import com.trendy.cbs.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
}
