package com.trendy.cbs.repos;

import com.trendy.cbs.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    Boolean existsByBranchName(String branchName);

    @Query("SELECT b.branchCode FROM Branch b WHERE b.branchCode LIKE CONCAT(:prefix, '%') ORDER BY b.branchCode DESC")
    String findLastBranchCode(@Param("prefix") String prefix);

}
