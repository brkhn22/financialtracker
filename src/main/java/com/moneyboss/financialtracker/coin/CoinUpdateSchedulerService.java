package com.moneyboss.financialtracker.coin;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinUpdateSchedulerService {

    private final CoinService coinService;
    
    // Available currencies to update
    private final List<String> currencies = Arrays.asList("USD", "TRY");
    
    // Track current position
    private final AtomicInteger currentCurrencyIndex = new AtomicInteger(0);
    private final AtomicInteger currentPage = new AtomicInteger(1);
    
    // Constants
    private static final int MAX_PAGE = 10;
    private static final int PER_PAGE = 250;

    @Scheduled(fixedRate = 45000) // Every 45 seconds
    public void updateCoinsScheduled() {
        try {
            String currentCurrency = getCurrentCurrency();
            int page = currentPage.get();
            
            log.info("Starting scheduled coin update for currency: {}, page: {}", currentCurrency, page);
            
            // Call the update method
            var response = coinService.updateAllCoins(currentCurrency, page, PER_PAGE);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                int updatedCount = response.getBody() != null ? response.getBody().size() : 0;
                log.info("Successfully updated {} coins for currency: {}, page: {}", 
                        updatedCount, currentCurrency, page);
            } else {
                log.warn("Failed to update coins for currency: {}, page: {}, status: {}", 
                        currentCurrency, page, response.getStatusCode());
            }
            
            // Move to next currency/page
            moveToNext();
            
        } catch (Exception e) {
            log.error("Error during scheduled coin update: {}", e.getMessage(), e);
            // Continue to next currency even if current one fails
            moveToNext();
        }
    }
    
    private String getCurrentCurrency() {
        int index = currentCurrencyIndex.get();
        return currencies.get(index);
    }
    
    private void moveToNext() {
        int currencyIndex = currentCurrencyIndex.get();
        int page = currentPage.get();
        
        // Move to next currency
        currencyIndex++;
        
        // If we've gone through all currencies
        if (currencyIndex >= currencies.size()) {
            currencyIndex = 0; // Reset to first currency
            page++; // Move to next page
            
            // If we've reached max page, reset to page 1
            if (page > MAX_PAGE) {
                page = 1;
                log.info("Completed all pages for all currencies. Starting over from page 1.");
            }
            
            currentPage.set(page);
        }
        
        currentCurrencyIndex.set(currencyIndex);
        
        String nextCurrency = currencies.get(currencyIndex);
        log.info("Next scheduled update: currency: {}, page: {}", nextCurrency, currentPage.get());
    }
    
    // Manual methods for monitoring
    public String getCurrentStatus() {
        return String.format("Current: %s, Page: %d", getCurrentCurrency(), currentPage.get());
    }
    
    public void resetScheduler() {
        currentCurrencyIndex.set(0);
        currentPage.set(1);
        log.info("Scheduler reset to USD, page 1");
    }
}
