package com.moneyboss.financialtracker.user.debt_user;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebtUserResponse {
    private Integer id;
    private String name;
    private Category category;
    private Double totalAmount;
    private Double monthlyPayment;
    private Integer remainingMonths;
    private Double interestRate;
    private LocalDateTime insertedAt;
}
