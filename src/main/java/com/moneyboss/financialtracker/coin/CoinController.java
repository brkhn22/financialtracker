package com.moneyboss.financialtracker.coin;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/coins")
@RequiredArgsConstructor
public class CoinController {

    private final CoinService coinService;

    @GetMapping("/fetch-all-coins")
    public ResponseEntity<List<Coin>> fetchAllCoins(
        @RequestParam(defaultValue = "usd") String currency,
        @RequestParam(defaultValue = "250") Integer perPage,
        @RequestParam(defaultValue = "1") Integer page) {
        
        return coinService.fetchAllCoinsByCurrency(currency, page, perPage);
    }

    @GetMapping("/get-popular-coins")
    public ResponseEntity<?> getPopularCoins(
        @RequestParam(defaultValue = "usd") String currency) {
        try {
            return coinService.getPopularCoins(currency);
        } catch (Exception e) {
            // Add explicit error handling
            return ResponseEntity.ok(Map.of(
                "error", "Failed to fetch coins: " + e.getMessage(),
                "coins", List.of()
            ));
        }
    }

    @GetMapping("/search-coins")
    public ResponseEntity<List<Coin>> searchCoins(
        @RequestParam String query,
        @RequestParam(defaultValue = "usd") String currency){
        return coinService.getCoinsByQuery(query, currency);
    }

    @GetMapping("/get-coin-by-id")
    public ResponseEntity<Coin> getCoinById(
        @RequestParam String id,
        @RequestParam(defaultValue = "usd") String currency) {
        return coinService.getCoinById(id, currency);
    }
}
