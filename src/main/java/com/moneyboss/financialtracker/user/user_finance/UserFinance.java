package com.moneyboss.financialtracker.user.user_finance;

import com.moneyboss.financialtracker.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_finance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFinance {

    @Id
    private Integer userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "monthly_income")
    private Double monthlyIncome;
    
    @Column(name = "current_saving")
    private Double currentSaving;
    
    @Column(name = "investment_goal")
    private Double investmentGoal;
}
