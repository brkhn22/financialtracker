package com.moneyboss.financialtracker.email;

public interface EmailSender {
    void send(String to, String email);
}
