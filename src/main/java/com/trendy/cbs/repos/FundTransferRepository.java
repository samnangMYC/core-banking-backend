package com.trendy.cbs.repos;

import com.trendy.cbs.entity.FundTransfer;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FundTransferRepository extends JpaRepository<FundTransfer, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select f from FundTransfer f where f.id = :id")
    Optional<FundTransfer> findByIdForUpdate(@Param("id") Long id);
}
