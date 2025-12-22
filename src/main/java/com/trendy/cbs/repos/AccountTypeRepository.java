
package com.trendy.cbs.repos;

import com.trendy.cbs.entity.AccountType;
import com.trendy.cbs.enums.PurposeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
    Optional<AccountType> findByCode(String code);

    Optional<AccountType> findByPurposeType(PurposeType purposeType);
}
