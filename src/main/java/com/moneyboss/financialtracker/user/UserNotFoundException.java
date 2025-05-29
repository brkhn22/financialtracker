package com.moneyboss.financialtracker.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super("User error: "+message);
    }

}
