package com.trendy.cbs.repos;

import com.trendy.cbs.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(@NotBlank(message = "Username is required") @Size(min = 4, max = 50) String username);

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email String email);

    User findByAuthProviderUserId(String authProviderUserId);
}
