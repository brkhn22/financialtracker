package com.moneyboss.financialtracker.auth;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }
    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
