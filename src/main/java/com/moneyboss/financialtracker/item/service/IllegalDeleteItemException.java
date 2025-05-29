package com.moneyboss.financialtracker.item.service;

public class IllegalDeleteItemException extends RuntimeException {

    public IllegalDeleteItemException(String message) {
        super("Delete Item error: "+message);
    }

}
