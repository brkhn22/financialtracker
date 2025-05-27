package com.moneyboss.financialtracker.item.service;

public class IllegalUpdateItemException extends RuntimeException {

    public IllegalUpdateItemException(String message) {
        super(message);
    }

    public IllegalUpdateItemException(String message, Throwable cause) {
        super(message, cause);
    }

}
