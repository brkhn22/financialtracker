package com.moneyboss.financialtracker.user.user_finance;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFinanceRepository extends JpaRepository<UserFinance, Long> {
    Optional<UserFinance> findByUserId(Integer userId);
}
