package com.moneyboss.financialtracker.user.expense_category;

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
@Table(name = "expense_category_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseCategoryUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "monthly_budget", nullable = false)
    private Double monthlyBudget;

    @Column(name = "icon_code_point", nullable = false)
    private String iconCodePoint;

    @Column(name = "color_value", nullable = false)
    private String colorValue;

    @Column(name = "is_essential", nullable = false)
    private Boolean isEssential;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User user;
}