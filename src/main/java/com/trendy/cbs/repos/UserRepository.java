package com.trendy.cbs.repos;

import com.trendy.cbs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
