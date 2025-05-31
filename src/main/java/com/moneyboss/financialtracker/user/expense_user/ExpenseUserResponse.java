package com.moneyboss.financialtracker.user.expense_user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseUserResponse {

    private Integer id;
    private String name;
    private Double expenseAmount;
}
