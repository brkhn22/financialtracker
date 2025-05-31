package com.moneyboss.financialtracker.user.debt_user;

import java.time.LocalDateTime;

import com.moneyboss.financialtracker.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "debt_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebtUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private Category category;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "monthly_payment")
    private Double monthlyPayment;

    @Column(name = "remaining_months")
    private Integer remainingMonths;

    @Column(name = "interest_rate")
    private Double interestRate;

    @Column(name = "inserted_at")
    private LocalDateTime insertedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
