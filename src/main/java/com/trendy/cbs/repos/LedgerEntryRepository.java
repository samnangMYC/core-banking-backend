package com.trendy.cbs.repos;

import com.trendy.cbs.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

    @Query(value = "SELECT nextval('ledger_txn_seq')", nativeQuery = true)
    String next();
}
