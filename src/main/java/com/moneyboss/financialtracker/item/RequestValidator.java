package com.moneyboss.financialtracker.item;

import com.moneyboss.financialtracker.item.item_user.AddItemUserRequest;
import com.moneyboss.financialtracker.item.service.IllegalAddItemException;
import com.moneyboss.financialtracker.item.service.IllegalUpdateItemException;
import com.moneyboss.financialtracker.item.service.IllegalUserItemException;

public class RequestValidator {


    public static void validateAddItemUserRequest(AddItemUserRequest request) {
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

    public static void validateUpdateItemRequest(UpdateItemRequest request) {
        if (request.getOriginalName() == null || request.getOriginalName().isEmpty()) {
            throw new IllegalUpdateItemException("Original item name cannot be null or empty.");
        }
        if(request.getNewSymbolPath() != null){
            if (request.getNewSymbolPath().isEmpty()) {
                throw new IllegalUpdateItemException("New symbol path cannot be empty.");
            }
        }
        if(request.getNewName() != null){
            if (request.getNewName().isEmpty()) {
                throw new IllegalUpdateItemException("New item name cannot be empty.");
            }else if (request.getNewName().equals(request.getOriginalName())) {
                throw new IllegalUpdateItemException("New item name cannot be the same as the original name.");
            }
        }
    }
}
