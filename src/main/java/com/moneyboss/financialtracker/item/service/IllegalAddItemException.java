package com.moneyboss.financialtracker.item.service;

public class IllegalAddItemException extends RuntimeException {
    public IllegalAddItemException(String message) {
        super("Item add error: "+message);
    }
}
