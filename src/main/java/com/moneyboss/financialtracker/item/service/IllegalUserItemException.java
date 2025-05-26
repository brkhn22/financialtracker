package com.moneyboss.financialtracker.item.service;

public class IllegalUserItemException extends RuntimeException {

    public IllegalUserItemException(String message) {
        super(message);
    }

    public IllegalUserItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalUserItemException(Throwable cause) {
        super(cause);
    }

}
