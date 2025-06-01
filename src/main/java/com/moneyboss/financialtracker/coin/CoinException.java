package com.moneyboss.financialtracker.coin;

public class CoinException extends RuntimeException {

    public CoinException(String message) {
        super("Coin error: "+message);
    }


}
