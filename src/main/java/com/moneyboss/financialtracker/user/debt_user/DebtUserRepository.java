package com.moneyboss.financialtracker.user.debt_user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DebtUserRepository extends JpaRepository<DebtUser, Integer>{

    Optional<List<DebtUser>> findByUserId(Integer userId);
    Optional<DebtUser> findById(Integer id);
}
