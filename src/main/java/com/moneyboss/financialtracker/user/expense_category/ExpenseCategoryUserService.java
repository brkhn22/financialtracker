package com.moneyboss.financialtracker.user.expense_category;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.moneyboss.financialtracker.user.UserNotFoundException;
import com.moneyboss.financialtracker.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryUserService {

    private final ExpenseCategoryUserRepository expenseCategoryUserRepository;
    private final UserRepository userRepository;

    // Method to get all expense categories of the current user
    public ResponseEntity<List<ExpenseCategoryUserResponse>> getAllExpenseCategoriesOfCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        List<ExpenseCategoryUserResponse> expenseCategories = expenseCategoryUserRepository.findByUserId(user.getId())
            .orElseThrow(() -> new UserNotFoundException("No expense categories found for user with id: " + user.getId()))
            .stream()
            .map(category -> new ExpenseCategoryUserResponse(
                category.getId(),
                category.getName(),
                category.getMonthlyBudget(),
                category.getIconCodePoint(),
                category.getColorValue(),
                category.getIsEssential()
            ))
            .toList();

        return ResponseEntity.ok(expenseCategories);
    }

    public ResponseEntity<ExpenseCategoryUserResponse> createExpenseCategory(ExpenseCategoryUserRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new ExpenseCategoryException("Category name is required");
        }
        if (request.getMonthlyBudget() == null || request.getMonthlyBudget() <= 0) {
            throw new ExpenseCategoryException("Monthly budget must be greater than zero");
        }
        if (request.getIconCodePoint() == null || request.getIconCodePoint().trim().isEmpty()) {
            throw new ExpenseCategoryException("Icon code point is required");
        }
        if (request.getColorValue() == null || request.getColorValue().trim().isEmpty()) {
            throw new ExpenseCategoryException("Color value is required");
        }
        if (request.getIsEssential() == null) {
            throw new ExpenseCategoryException("Essential flag is required");
        }

        ExpenseCategoryUser expenseCategory = new ExpenseCategoryUser();
        expenseCategory.setName(request.getName().trim());
        expenseCategory.setMonthlyBudget(request.getMonthlyBudget());
        expenseCategory.setIconCodePoint(request.getIconCodePoint().trim());
        expenseCategory.setColorValue(request.getColorValue().trim());
        expenseCategory.setIsEssential(request.getIsEssential());
        expenseCategory.setUser(user);

        expenseCategoryUserRepository.save(expenseCategory);

        return ResponseEntity.ok(new ExpenseCategoryUserResponse(
            expenseCategory.getId(),
            expenseCategory.getName(),
            expenseCategory.getMonthlyBudget(),
            expenseCategory.getIconCodePoint(),
            expenseCategory.getColorValue(),
            expenseCategory.getIsEssential()
        ));
    }

    public ResponseEntity<ExpenseCategoryUserResponse> deleteExpenseCategory(ExpenseCategoryIdRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        ExpenseCategoryUser expenseCategory = expenseCategoryUserRepository.findById(request.getId())
            .orElseThrow(() -> new ExpenseCategoryException("Expense category not found with id: " + request.getId()));
        
        if (!expenseCategory.getUser().getId().equals(user.getId())) {
            throw new UserNotFoundException("Expense category with id: " + request.getId() + " does not belong to user with id: " + user.getId());
        }

        expenseCategoryUserRepository.delete(expenseCategory);
        
        return ResponseEntity.ok(new ExpenseCategoryUserResponse(
            expenseCategory.getId(),
            expenseCategory.getName(),
            expenseCategory.getMonthlyBudget(),
            expenseCategory.getIconCodePoint(),
            expenseCategory.getColorValue(),
            expenseCategory.getIsEssential()
        ));
    }

    public ResponseEntity<ExpenseCategoryUserResponse> updateExpenseCategory(ExpenseCategoryUpdateRequest request) {
        if (request.getId() == null) {
            throw new ExpenseCategoryException("Category id is required");
        }

        if (request.getName() == null && request.getMonthlyBudget() == null && 
            request.getIconCodePoint() == null && request.getColorValue() == null && 
            request.getIsEssential() == null) {
            throw new ExpenseCategoryException("At least one field must be provided for update");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        ExpenseCategoryUser expenseCategory = expenseCategoryUserRepository.findById(request.getId())
            .orElseThrow(() -> new ExpenseCategoryException("Expense category not found with id: " + request.getId()));
        
        if (!expenseCategory.getUser().getId().equals(user.getId())) {
            throw new UserNotFoundException("Expense category with id: " + request.getId() + " does not belong to user with id: " + user.getId());
        }

        // Update fields if provided
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            expenseCategory.setName(request.getName().trim());
        }
        if (request.getMonthlyBudget() != null && request.getMonthlyBudget() > 0) {
            expenseCategory.setMonthlyBudget(request.getMonthlyBudget());
        }
        if (request.getIconCodePoint() != null && !request.getIconCodePoint().trim().isEmpty()) {
            expenseCategory.setIconCodePoint(request.getIconCodePoint().trim());
        }
        if (request.getColorValue() != null && !request.getColorValue().trim().isEmpty()) {
            expenseCategory.setColorValue(request.getColorValue().trim());
        }
        if (request.getIsEssential() != null) {
            expenseCategory.setIsEssential(request.getIsEssential());
        }

        expenseCategoryUserRepository.save(expenseCategory);

        return ResponseEntity.ok().body(
            ExpenseCategoryUserResponse.builder()
                .id(expenseCategory.getId())
                .name(expenseCategory.getName())
                .monthlyBudget(expenseCategory.getMonthlyBudget())
                .iconCodePoint(expenseCategory.getIconCodePoint())
                .colorValue(expenseCategory.getColorValue())
                .isEssential(expenseCategory.getIsEssential())
                .build()
        );
    }
}