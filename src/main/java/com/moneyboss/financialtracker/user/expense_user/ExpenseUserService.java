package com.moneyboss.financialtracker.user.expense_user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.moneyboss.financialtracker.user.UserNotFoundException;
import com.moneyboss.financialtracker.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseUserService {

    private final ExpenseUserRepository expenseUserRepository;
    private final UserRepository userRepository;
    // Method to get all expenses of the current user
    public ResponseEntity<List<ExpenseUserResponse>> getAllExpensesOfCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        List<ExpenseUserResponse> expenses = expenseUserRepository.findByUserId(user.getId())
            .orElseThrow(() -> new UserNotFoundException("No expenses found for user with id: " + user.getId()))
            .stream()
            .map(expense -> new ExpenseUserResponse(
                expense.getId(),
                expense.getName(),
                expense.getExpenseAmount()
            ))
            .toList();

        return ResponseEntity.ok(expenses);
    }

    public ResponseEntity<ExpenseUserResponse> createExpense(ExpenseUserRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        if (request.getName() == null || request.getExpenseAmount() == null || request.getName().trim().isEmpty()) {
            throw new ExpenseException("All fields must be provided");
        }
        if (request.getExpenseAmount() <= 0) {
            throw new ExpenseException("Expense amount must be greater than zero");
        }

        ExpenseUser expense = new ExpenseUser();
        expense.setName(request.getName());
        expense.setExpenseAmount(request.getExpenseAmount());
        expense.setUser(user);

        expenseUserRepository.save(expense);

        return ResponseEntity.ok(new ExpenseUserResponse(
            expense.getId(),
            expense.getName(),
            expense.getExpenseAmount()
        ));
    }

    public ResponseEntity<ExpenseUserResponse> deleteExpense(ExpenseIdRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        ExpenseUser expense = expenseUserRepository.findById(request.getId())
            .orElseThrow(() -> new ExpenseException("Expense not found with id: " + request.getId()));
        if (!expense.getUser().getId().equals(user.getId())) {
            throw new UserNotFoundException("Expense with id: " + request.getId() + " does not belong to user with id: " + user.getId());
        }

        expenseUserRepository.delete(expense);
        return ResponseEntity.ok(new ExpenseUserResponse(
            expense.getId(),
            expense.getName(),
            expense.getExpenseAmount()
        ));
    }

    public ResponseEntity<ExpenseUserResponse> updateExpense(ExpenseUpdateRequest request){
        if(request.getId() == null)
            throw new ExpenseException("User id is required");
        
        if(request.getName() == null && request.getExpenseAmount() == null)
            throw new ExpenseException("One of the fields must be submitted");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
                var user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        ExpenseUser expense = expenseUserRepository.findById(request.getId())
            .orElseThrow(() -> new ExpenseException("Expense not found with id: " + request.getId()));
        if (!expense.getUser().getId().equals(user.getId())) {
            throw new UserNotFoundException("Expense with id: " + request.getId() + " does not belong to user with id: " + user.getId());
        }

        if(request.getExpenseAmount() != null && request.getExpenseAmount() > 0)
            expense.setExpenseAmount(request.getExpenseAmount());
        if(request.getName() != null && !request.getName().isEmpty())
            expense.setName(request.getName());
        
        expenseUserRepository.save(expense);

        return ResponseEntity.ok().body(
            ExpenseUserResponse.builder()
            .expenseAmount(expense.getExpenseAmount())
            .name(expense.getName())
            .id(expense.getId())
            .build());

    }
}
