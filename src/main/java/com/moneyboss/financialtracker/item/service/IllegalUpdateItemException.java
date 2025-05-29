package com.moneyboss.financialtracker.item.service;

public class IllegalUpdateItemException extends RuntimeException {

    public IllegalUpdateItemException(String message) {
        super("Item update error: "+message);
    }

}
