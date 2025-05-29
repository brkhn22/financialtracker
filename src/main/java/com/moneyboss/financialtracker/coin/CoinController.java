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

    @GetMapping("/get-popular-coins")
    public ResponseEntity<?> getPopularCoins() {
        try {
            return coinService.getPopularCoins();
        } catch (Exception e) {
            // Add explicit error handling
            return ResponseEntity.ok(Map.of(
                "error", "Failed to fetch coins: " + e.getMessage(),
                "coins", List.of()
            ));
        }
    }

    @GetMapping("/search-coins")
    public ResponseEntity<SearchCoinResponse> searchCoins(@RequestParam String query) {
        return coinService.getCoinsByQuery(query);
    }
    
    // Add a test endpoint to verify the issue
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        return ResponseEntity.ok(Map.of("status", "working"));
    }
}
