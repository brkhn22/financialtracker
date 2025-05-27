package com.moneyboss.financialtracker.item.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moneyboss.financialtracker.item.AddItemRequest;
import com.moneyboss.financialtracker.item.AddItemResponse;
import com.moneyboss.financialtracker.item.UpdateItemRequest;
import com.moneyboss.financialtracker.item.UpdateItemResponse;
import com.moneyboss.financialtracker.item.item_user.AddItemUserRequest;
import com.moneyboss.financialtracker.item.item_user.AddItemUserResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/update-item")
    public ResponseEntity<UpdateItemResponse> updateItem(
        @RequestBody UpdateItemRequest request
    ) {
        // Logic to update an item
        return itemService.updateItem(request);
    }

    @GetMapping("/get-all-items")
    public ResponseEntity<UserItemResponse> getUserItems() {
        // Logic to fetch items for the user
        return itemService.getItems();
    }
    
    @PostMapping("/add-user-item")
    public ResponseEntity<AddItemUserResponse> addItemForUser(
        @RequestBody AddItemUserRequest request
    ) {
        // Logic to add an item for the user
        return itemService.addItemByUserId(request);
    }

    @PostMapping("/add-item")
    public ResponseEntity<AddItemResponse> addItem(
        @RequestBody AddItemRequest request
    ) {
        // Logic to add a new item
        return itemService.addItem(request);
    }
    
}