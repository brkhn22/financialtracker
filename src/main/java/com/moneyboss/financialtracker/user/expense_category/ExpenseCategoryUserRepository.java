package com.moneyboss.financialtracker.user.expense_category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseCategoryUserRepository extends JpaRepository<ExpenseCategoryUser, Integer> {

    Optional<List<ExpenseCategoryUser>> findByUserId(Integer userId);
    Optional<ExpenseCategoryUser> findById(Integer id);
}