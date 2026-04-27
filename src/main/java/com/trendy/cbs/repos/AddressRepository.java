package com.trendy.cbs.repos;

import com.trendy.cbs.entity.Address;
import com.trendy.cbs.entity.Customer;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
