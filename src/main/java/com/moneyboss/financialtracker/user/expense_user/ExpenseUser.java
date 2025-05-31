package com.moneyboss.financialtracker.user.expense_user;
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
@Table(name = "expense_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "expense_amount")
    private Double expenseAmount;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User user;
}
