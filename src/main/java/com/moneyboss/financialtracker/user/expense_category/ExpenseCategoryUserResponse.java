package com.moneyboss.financialtracker.user.expense_category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseCategoryUserResponse {
    private Integer id;
    private String name;
    private Double monthlyBudget;
    private String iconCodePoint;
    private String colorValue;
    private Boolean isEssential;
}