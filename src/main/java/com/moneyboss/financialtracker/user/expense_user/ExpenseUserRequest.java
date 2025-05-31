package com.moneyboss.financialtracker.user.expense_user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseUserRequest {
    private String name;
    private Double expenseAmount;
}
