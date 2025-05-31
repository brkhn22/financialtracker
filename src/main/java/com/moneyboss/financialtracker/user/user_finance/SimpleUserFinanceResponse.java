package com.moneyboss.financialtracker.user.user_finance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUserFinanceResponse {
    private Integer userId;
    private Double monthlyIncome;
    private Double currentSaving;
    private Double investmentGoal;
}
