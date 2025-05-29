package com.moneyboss.financialtracker.item.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moneyboss.financialtracker.item.AddItemRequest;
import com.moneyboss.financialtracker.item.ItemResponse;
import com.moneyboss.financialtracker.item.ItemIdRequest;
import com.moneyboss.financialtracker.item.ItemsResponse;
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
        return itemService.updateItem(request);
    }

    @GetMapping("/get-all-items")
    public ResponseEntity<ItemsResponse> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/get-items-of-user")
    public ResponseEntity<UserItemResponse> getUserItems() {
        return itemService.getItems();
    }
    
    @PostMapping("/add-user-item")
    public ResponseEntity<AddItemUserResponse> addItemForUser(
        @RequestBody AddItemUserRequest request
    ) {
        return itemService.addItemByUserId(request);
    }

    @PostMapping("/add-item")
    public ResponseEntity<ItemResponse> addItem(
        @RequestBody AddItemRequest request
    ) {
        return itemService.addItem(request);
    }

    @PostMapping("/delete-item")
    public ResponseEntity<ItemResponse> deleteItem(
        @RequestBody ItemIdRequest request
    ) {
        return itemService.deleteItemById(request);
    }
}