package com.trendy.cbs.repos;

import com.trendy.cbs.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
