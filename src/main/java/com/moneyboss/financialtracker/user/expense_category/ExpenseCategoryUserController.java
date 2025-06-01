package com.moneyboss.financialtracker.user.expense_category;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/expense-categories")
@RequiredArgsConstructor
public class ExpenseCategoryUserController {

    private final ExpenseCategoryUserService expenseCategoryUserService;
    
    @GetMapping("get-all-expense-categories")
    public ResponseEntity<List<ExpenseCategoryUserResponse>> getAllExpenseCategoriesOfCurrentUser() {
        return expenseCategoryUserService.getAllExpenseCategoriesOfCurrentUser();
    }

    @PostMapping("create-expense-category")
    public ResponseEntity<ExpenseCategoryUserResponse> createExpenseCategory(
        @RequestBody ExpenseCategoryUserRequest request) {
        return expenseCategoryUserService.createExpenseCategory(request);
    }

    @PostMapping("delete-expense-category-by-id")
    public ResponseEntity<ExpenseCategoryUserResponse> deleteExpenseCategoryById(
        @RequestBody ExpenseCategoryIdRequest request
    ) {
        return expenseCategoryUserService.deleteExpenseCategory(request);
    }

    @PostMapping("update-expense-category-by-id")
    public ResponseEntity<ExpenseCategoryUserResponse> updateExpenseCategoryById(
        @RequestBody ExpenseCategoryUpdateRequest request
    ) {
        return expenseCategoryUserService.updateExpenseCategory(request);
    }
}