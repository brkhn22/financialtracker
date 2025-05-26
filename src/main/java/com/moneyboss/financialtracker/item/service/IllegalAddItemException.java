package com.moneyboss.financialtracker.item.service;

public class IllegalAddItemException extends RuntimeException {
    public IllegalAddItemException(String message) {
        super(message);
    }

    public IllegalAddItemException(String message, Throwable cause) {
        super(message, cause);
    }

}
