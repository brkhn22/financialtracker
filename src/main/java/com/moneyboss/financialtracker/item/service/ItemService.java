package com.moneyboss.financialtracker.item.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.moneyboss.financialtracker.coin.Coin;
import com.moneyboss.financialtracker.coin.CoinService;
import com.moneyboss.financialtracker.item.RequestValidator;
import com.moneyboss.financialtracker.item.item_user.AddItemUserRequest;
import com.moneyboss.financialtracker.item.item_user.ItemCoin;
import com.moneyboss.financialtracker.item.item_user.ItemUser;
import com.moneyboss.financialtracker.item.item_user.ItemUserRepository;
import com.moneyboss.financialtracker.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemUserRepository itemUserRepository;
    private final UserRepository userRepository;
    private final CoinService coinService;

    public ResponseEntity<List<ItemCoin>> getItems(String currency) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Integer userId = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username))
                .getId();

        List<ItemUser> items = itemUserRepository.findByUserId(userId)
                .orElseThrow(() -> new ItemNotFoundException("No items found for user with ID: " + userId));

        var coins = coinService.getAllCoins(currency).getBody();

        List<ItemCoin> itemCoins = items.stream()
                .map(item -> {
                    Coin coin = coins.stream()
                            .filter(c -> c.getId().equals(item.getItemId()))
                            .sorted((c1, c2) -> c1.getLastUpdated().compareTo(c2.getLastUpdated()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Coin not found for item ID: " + item.getItemId()));
                    return new ItemCoin(
                        coin.getId(),
                        coin.getName(),
                        coin.getSymbol(),
                        coin.getImage(),
                        coin.getCurrentPrice(),
                        coin.getPriceChangePercentage24h(),
                        coin.getPriceChange24h(),
                        coin.getLastUpdated(),
                        item.getBuyingPrice(),
                        item.getQuantity(),
                        item.getInsertedAt()
                    );
                })
                .toList();
        return ResponseEntity.ok().body(itemCoins);
    }

    public ResponseEntity<ItemCoin> addItemByUserId(AddItemUserRequest request) {
        RequestValidator.validateAddItemUserRequest(request);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        var coin = coinService.getCoinById(request.getItemId(), request.getCurrency()).getBody();
        
        if(coin == null) throw new RuntimeException("Coin is not found!");
        
        

        ItemUser itemUser = ItemUser.builder()
                .user(user)
                .itemId(request.getItemId())
                .quantity(request.getQuantity())
                .buyingPrice(request.getBuyingPrice())
                .insertedAt(LocalDateTime.now())
                .build();
        itemUserRepository.save(itemUser);
        
        return ResponseEntity.ok().body(ItemCoin.builder()
                .coinId(coin.getId())
                .coinName(coin.getName())
                .coinSymbol(coin.getSymbol())
                .coinImage(coin.getImage())
                .coinCurrentPrice(coin.getCurrentPrice())
                .coinPriceChangePercentage24h(coin.getPriceChangePercentage24h())
                .coinPriceChange24h(coin.getPriceChange24h())
                .coinLastUpdated(coin.getLastUpdated())
                .coinBuyingPrice(request.getBuyingPrice())
                .coinQuantity(request.getQuantity())
                .coinInsertedAt(itemUser.getInsertedAt())
                .build());
    }

    public ResponseEntity<DecreaseQuantityResponse> decreaseItemQuantity(DecreaseQuantityRequest request) {
        RequestValidator.validateDecreaseItemQuantityRequest(request);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        var itemUser = itemUserRepository.findByItemIdAndUserId(request.getItemId(), user.getId())
                .orElseThrow(() -> new ItemNotFoundException("Item not found for user with ID: " + user.getId()));

        if (itemUser.size() <= 0) 
            throw new RuntimeException("There are no items called: " + request.getItemId());

        
        Double totalQuantity = itemUser.stream()
                .mapToDouble(ItemUser::getQuantity)
                .sum();
        Double quantity = request.getQuantity();
        if(totalQuantity < quantity)
                throw new RuntimeException("There are no enough coin");
        
        // Sort by insertedAt to ensure we remove the oldest items first
        Collections.sort(itemUser, (a, b) -> a.getInsertedAt().compareTo(b.getInsertedAt()));

        Coin coin = coinService.getCoinById(request.getItemId(), request.getCurrency()).getBody();
        Double totalPrice = 0.0;
        for(int i = 0; i < itemUser.size() && quantity > 0; i++){
                ItemUser tempItemUser = itemUser.get(i);
                if (tempItemUser.getQuantity() <= quantity) {
                        quantity -= tempItemUser.getQuantity();
                        tempItemUser.setQuantity(0.0);
                        totalPrice += (coin.getCurrentPrice()-tempItemUser.getBuyingPrice())*tempItemUser.getQuantity();
                }else{
                        Double difference = tempItemUser.getQuantity() - quantity;
                        totalPrice += (coin.getCurrentPrice()-tempItemUser.getBuyingPrice())*difference;
                        tempItemUser.setQuantity(difference);
                        quantity = 0.0;
                }
        }
        
        itemUser.forEach(itemUserTemp -> {
                if (itemUserTemp.getQuantity() <= 0) 
                        itemUserRepository.delete(itemUserTemp);
                else 
                        itemUserRepository.save(itemUserTemp);
            
        });

        return ResponseEntity.ok(
                DecreaseQuantityResponse
                .builder()
                .totalPrice(totalPrice)
                .quantityRemoved(request.getQuantity())
                .coin(coin)
                .build()
                );
    }
}
