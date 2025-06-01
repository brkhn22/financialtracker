package com.moneyboss.financialtracker.user.expense_category;

public class ExpenseCategoryException extends RuntimeException {

    public ExpenseCategoryException(String message) {
        super("Expense Category error: " + message);
    }
}