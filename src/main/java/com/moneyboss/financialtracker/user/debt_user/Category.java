package com.moneyboss.financialtracker.user.debt_user;

import lombok.Getter;

@Getter
public enum Category {
    PERSONAL("Personal"),
    HOUSE("House"),
    CAR("Car"),
    CREDIT_CARD("Credit Card"),
    EDUCATION("Education"),
    OTHER("Other");

    private final String name;
    private Category(String name) {
        this.name = name;
    }
}
