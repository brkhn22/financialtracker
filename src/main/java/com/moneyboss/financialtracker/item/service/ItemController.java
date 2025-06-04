package com.moneyboss.financialtracker.item.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moneyboss.financialtracker.item.item_user.AddItemUserRequest;
import com.moneyboss.financialtracker.item.item_user.ItemCoin;
import com.moneyboss.financialtracker.item.item_user.ItemUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/get-items-of-user")
    public ResponseEntity<List<ItemCoin>> getUserItems(
        @RequestParam(defaultValue = "usd") String currency
    ) {
        return itemService.getItems(currency);
    }
    
    @PostMapping("/add-user-item")
    public ResponseEntity<ItemCoin> addItemForUser(
        @RequestBody AddItemUserRequest request
    ) {
        return itemService.addItemByUserId(request);
    }

    @PostMapping("/decrease-item-quantity")
    public ResponseEntity<DecreaseQuantityResponse> decreaseItemForUser(
        @RequestBody DecreaseQuantityRequest request){
            return itemService.decreaseItemQuantity(request);
    }

    @PostMapping("/delete-user-item")
    public ResponseEntity<ItemUser> deleteItemForUser(
        @RequestBody ItemUserIdRequest request
    ) {
        return itemService.deleteItem(request);
    }
}