package com.moneyboss.financialtracker.user.user_finance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserFinanceRequest {
    private Double monthlyIncome;
    private Double currentSaving;
    private Double investmentGoal;
}
