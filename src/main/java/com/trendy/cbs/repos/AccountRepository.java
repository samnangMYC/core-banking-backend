package com.trendy.cbs.repos;

import com.trendy.cbs.entity.Account;
import com.trendy.cbs.entity.AccountType;
import com.trendy.cbs.entity.Currency;
import com.trendy.cbs.entity.Customer;
import com.trendy.cbs.payload.dto.AccountDTO;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {

    Boolean existsByCustomer(Customer customer);

    Optional<Account> findByAccNumber(String accNumber);

    boolean existsByAccNumber(String accountNumber);


    Integer countByCustomerCusId(Long customerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.accId = :id")
    Optional<Account> findByIdForUpdate(@Param("id") Long id);
}
