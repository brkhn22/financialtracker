package com.moneyboss.financialtracker.user.debt_user;

public class DebtException extends RuntimeException {

    public DebtException(String message) {
        super("Debt error: "+message);
    }

}
