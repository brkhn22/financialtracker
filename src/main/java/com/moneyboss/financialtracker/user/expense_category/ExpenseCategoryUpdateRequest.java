package com.moneyboss.financialtracker.user.expense_category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCategoryUpdateRequest {
    private Integer id;
    private String name;
    private Double monthlyBudget;
    private String iconCodePoint;
    private String colorValue;
    private Boolean isEssential;
}