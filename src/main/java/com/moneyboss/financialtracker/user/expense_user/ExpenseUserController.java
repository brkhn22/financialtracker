package com.moneyboss.financialtracker.user.expense_user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseUserController {

    private final ExpenseUserService expenseUserService;
    
    @GetMapping("get-all-expenses")
    public ResponseEntity<List<ExpenseUserResponse>> getAllExpensesOfCurrentUser() {
        return expenseUserService.getAllExpensesOfCurrentUser();
    }

    @PostMapping("create-expense")
    public ResponseEntity<ExpenseUserResponse> createExpense(
        @RequestBody ExpenseUserRequest request) {
        return expenseUserService.createExpense(request);
    }

    @PostMapping("delete-expense-by-id")
    public ResponseEntity<ExpenseUserResponse> deleteExpenseById(
        @RequestBody ExpenseIdRequest request
    ) {
        return expenseUserService.deleteExpense(request);
    }

    @PostMapping("update-expense-by-id")
    public ResponseEntity<ExpenseUserResponse> updateExpenseById(
        @RequestBody ExpenseUpdateRequest request
    ){
        return expenseUserService.updateExpense(request);
    }
}
