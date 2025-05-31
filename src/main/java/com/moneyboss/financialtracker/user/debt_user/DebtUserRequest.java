package com.moneyboss.financialtracker.user.debt_user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtUserRequest {
    private String name;
    private Category category;
    private Double totalAmount;
    private Double monthlyPayment;
    private Integer remainingMonths;
    private Double interestRate;
    
}
