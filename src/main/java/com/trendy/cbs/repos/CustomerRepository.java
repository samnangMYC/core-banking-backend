package com.trendy.cbs.repos;

import com.trendy.cbs.entity.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByPhoneNumber(@NotBlank(message = "Phone number is required") @Size(max = 20) String phoneNumber);

    Optional<Customer> findByPhoneNumber(@NotBlank @Pattern(regexp = "^\\+?[0-9]{8,15}$") String phoneNumber);
}
