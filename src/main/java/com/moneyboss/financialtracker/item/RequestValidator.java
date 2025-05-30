package com.moneyboss.financialtracker.item;

import com.moneyboss.financialtracker.item.item_user.AddItemUserRequest;
import com.moneyboss.financialtracker.item.service.DecreaseQuantityRequest;
import com.moneyboss.financialtracker.item.service.IllegalUserItemException;

public class RequestValidator {


    public static void validateAddItemUserRequest(AddItemUserRequest request) {
        if (request.getItemId() == null || request.getItemId().isEmpty()) {
            throw new IllegalUserItemException("Item id cannot be null or empty.");
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalUserItemException("Quantity must be a positive number.");
        }
        
        if (request.getBuyingPrice() == null || request.getBuyingPrice() < 0) {
            throw new IllegalUserItemException("Buying price must be a non-negative number.");
        }
    }

    public static void validateDecreaseItemQuantityRequest(DecreaseQuantityRequest request){
        if(request.getItemId() == null || request.getItemId().isEmpty()){
            throw new IllegalUserItemException("Item id cannot be null or empty.");
        }

        if(request.getCurrency() == null || request.getCurrency().isEmpty()){
            throw new IllegalUserItemException("Currency cannot be null or empty.");

        }

        if(request.getQuantity() == null || request.getQuantity() <= 0.0){
            throw new IllegalUserItemException("Quantity cannot be null or 0.");

        }

    }


}
