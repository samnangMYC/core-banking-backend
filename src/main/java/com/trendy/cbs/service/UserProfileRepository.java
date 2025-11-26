package com.trendy.cbs.service;

import com.trendy.cbs.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    boolean existsByPhoneNumber(Integer phoneNumber);
}
