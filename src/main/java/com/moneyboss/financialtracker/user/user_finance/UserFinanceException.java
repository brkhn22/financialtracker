package com.moneyboss.financialtracker.user.user_finance;

public class UserFinanceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserFinanceException(String message) {
        super("User finance error: "+message);
    }

}
