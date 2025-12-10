package com.trendy.cbs.repos;

import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
