package com.moneyboss.financialtracker.item.service;

public class IllegalUserItemException extends RuntimeException {

    public IllegalUserItemException(String message) {
        super("User Item error: "+message);
    }

}
