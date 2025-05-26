package com.moneyboss.financialtracker.item.service;

import com.moneyboss.financialtracker.item.AddItemRequest;
import com.moneyboss.financialtracker.item.item_user.AddItemUserRequest;

public class RequestValidator {


    public static void validateAddItemUserRequest(AddItemUserRequest request) {
        if (request.getUserId() == null || request.getUserId() <= 0) {
            throw new IllegalUserItemException("User ID must be a positive integer.");
        }
        if (request.getItemName() == null || request.getItemName().isEmpty()) {
            throw new IllegalUserItemException("Item name cannot be null or empty.");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalUserItemException("Quantity must be a positive number.");
        }
        if (request.getBuyingPrice() == null || request.getBuyingPrice() < 0) {
            throw new IllegalUserItemException("Buying price must be a non-negative number.");
        }
    }
    
    public static void validateAddItemRequest(AddItemRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalAddItemException("Item name cannot be null or empty.");
        }
        if (request.getSymbolPath() == null || request.getSymbolPath().isEmpty()) {
            throw new IllegalAddItemException("Symbol path cannot be null or empty.");
        }

    }
}
