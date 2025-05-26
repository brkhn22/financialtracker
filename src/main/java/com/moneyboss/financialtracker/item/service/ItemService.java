package com.moneyboss.financialtracker.item.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.moneyboss.financialtracker.item.AddItemRequest;
import com.moneyboss.financialtracker.item.AddItemResponse;
import com.moneyboss.financialtracker.item.Item;
import com.moneyboss.financialtracker.item.ItemRepository;
import com.moneyboss.financialtracker.item.item_user.AddItemUserRequest;
import com.moneyboss.financialtracker.item.item_user.AddItemUserResponse;
import com.moneyboss.financialtracker.item.item_user.ItemUser;
import com.moneyboss.financialtracker.item.item_user.ItemUserRepository;
import com.moneyboss.financialtracker.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemUserRepository itemUserRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public UserItemResponse getItems() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Integer userId = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username))
                .getId();

        List<ItemUser> items = itemUserRepository.findByUserId(userId)
                .orElseThrow(() -> new ItemNotFoundException("No items found for user with ID: " + userId));

        return UserItemResponse.builder()
                .items(items)
                .build();  
    }

    public AddItemUserResponse addItemByUserId(AddItemUserRequest request) {
        RequestValidator.validateAddItemUserRequest(request);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        var item = itemRepository.findByName(request.getItemName())
                .orElseThrow(() -> new ItemNotFoundException("Item not found with name: " + request.getItemName()));

        ItemUser itemUser = ItemUser.builder()
                .user(user)
                .item(item)
                .quantity(request.getQuantity())
                .buyingPrice(request.getBuyingPrice())
                .insertedAt(LocalDateTime.now())
                .build();
        itemUserRepository.save(itemUser);
        
        return AddItemUserResponse.builder()
                .item(item)
                .quantity(request.getQuantity())
                .buyingPrice(request.getBuyingPrice())
                .build();
    }

    public AddItemResponse addItem(AddItemRequest request) {
        RequestValidator.validateAddItemRequest(request);
        
        var item = itemRepository.findByName(request.getName());
        if (item.isPresent()) {
            throw new IllegalArgumentException("Item with name " + request.getName() + " already exists");
        }

        Item bItem = Item.builder()
                .name(request.getName())
                .symbolPath(request.getSymbolPath())
                .build();

        itemRepository.save(bItem);
        
        return AddItemResponse.builder()
                .item(bItem)
                .build();
    }
}
