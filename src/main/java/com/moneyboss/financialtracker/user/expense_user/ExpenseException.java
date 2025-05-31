package com.moneyboss.financialtracker.user.expense_user;

public class ExpenseException extends RuntimeException {

    public ExpenseException(String message) {
        super("Expense error: "+message);
    }

}
