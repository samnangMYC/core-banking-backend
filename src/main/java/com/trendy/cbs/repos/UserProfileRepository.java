package com.trendy.cbs.repos;

import com.trendy.cbs.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    boolean existsByPhoneNumber(Integer phoneNumber);
}
