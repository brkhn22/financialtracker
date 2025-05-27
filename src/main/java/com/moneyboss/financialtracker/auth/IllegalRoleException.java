package com.moneyboss.financialtracker.auth;

public class IllegalRoleException extends RuntimeException {
    public IllegalRoleException(String message) {
        super(message);
    }

    public IllegalRoleException(String message, Throwable cause) {
        super(message, cause);
    }

}
