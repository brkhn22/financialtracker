package com.moneyboss.financialtracker.user.expense_user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseUserRepository extends JpaRepository<ExpenseUser, Integer> {

    Optional<List<ExpenseUser>> findByUserId(Integer userId);
    Optional<ExpenseUser> findById(Integer id);

}
