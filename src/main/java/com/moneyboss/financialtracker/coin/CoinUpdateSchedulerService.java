package com.moneyboss.financialtracker.coin;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
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
    private final AtomicInteger maxPage = new AtomicInteger(10); // Default to 10, can be changed
    
    // Scheduler control
    private ScheduledFuture<?> scheduledTask;
    private TaskScheduler taskScheduler;
    private volatile boolean isRunning = false;
    
    // Constants
    private static final int MIN_PAGE = 1;
    private static final int MAX_PAGE_LIMIT = 69;
    private static final int PER_PAGE = 250;
    private static final long FIXED_RATE = 45000; // 45 seconds

    // Initialize the task scheduler after construction
    @PostConstruct
    public void init() {
        this.taskScheduler = createTaskScheduler();
    }

    private TaskScheduler createTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("coin-update-scheduler-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.initialize();
        return scheduler;
    }

    public boolean startScheduler() {
        if (isRunning) {
            log.warn("Scheduler is already running");
            return false;
        }

        try {
            scheduledTask = taskScheduler.scheduleAtFixedRate(
                this::updateCoinsScheduled, 
                FIXED_RATE
            );
            isRunning = true;
            log.info("Coin update scheduler started successfully with max page: {}", maxPage.get());
            return true;
        } catch (Exception e) {
            log.error("Failed to start scheduler: {}", e.getMessage(), e);
            return false;
        }
    }

    public boolean stopScheduler() {
        if (!isRunning) {
            log.warn("Scheduler is not running");
            return false;
        }

        try {
            if (scheduledTask != null) {
                scheduledTask.cancel(false);
                scheduledTask = null;
            }
            isRunning = false;
            log.info("Coin update scheduler stopped successfully");
            return true;
        } catch (Exception e) {
            log.error("Failed to stop scheduler: {}", e.getMessage(), e);
            return false;
        }
    }

    public boolean restartScheduler() {
        log.info("Restarting scheduler...");
        stopScheduler();
        resetScheduler();
        return startScheduler();
    }

    private void updateCoinsScheduled() {
        try {
            String currentCurrency = getCurrentCurrency();
            int page = currentPage.get();
            
            log.info("Starting scheduled coin update for currency: {}, page: {}/{}", 
                    currentCurrency, page, maxPage.get());
            
            // Call the update method
            var response = coinService.updateAllCoins(currentCurrency, page, PER_PAGE);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                int updatedCount = response.getBody() != null ? response.getBody().size() : 0;
                log.info("Successfully updated {} coins for currency: {}, page: {}/{}", 
                        updatedCount, currentCurrency, page, maxPage.get());
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
            if (page > maxPage.get()) {
                page = 1;
                log.info("Completed all pages ({}) for all currencies. Starting over from page 1.", maxPage.get());
            }
            
            currentPage.set(page);
        }
        
        currentCurrencyIndex.set(currencyIndex);
        
        String nextCurrency = currencies.get(currencyIndex);
        log.info("Next scheduled update: currency: {}, page: {}/{}", 
                nextCurrency, currentPage.get(), maxPage.get());
    }
    
    // Status and control methods
    public String getCurrentStatus() {
        String status = isRunning ? "RUNNING" : "STOPPED";
        return String.format("Status: %s, Current: %s, Page: %d/%d, Next update in: %s", 
            status, getCurrentCurrency(), currentPage.get(), maxPage.get(),
            isRunning ? "~45 seconds" : "N/A");
    }
    
    public boolean isSchedulerRunning() {
        return isRunning;
    }
    
    public void resetScheduler() {
        currentCurrencyIndex.set(0);
        currentPage.set(1);
        log.info("Scheduler reset to USD, page 1 (max pages: {})", maxPage.get());
    }

    public String getDetailedStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Coin Update Scheduler Status ===\n");
        sb.append("Status: ").append(isRunning ? "RUNNING" : "STOPPED").append("\n");
        sb.append("Current Currency: ").append(getCurrentCurrency()).append("\n");
        sb.append("Current Page: ").append(currentPage.get()).append("/").append(maxPage.get()).append("\n");
        sb.append("Max Pages: ").append(maxPage.get()).append(" (Range: ").append(MIN_PAGE).append("-").append(MAX_PAGE_LIMIT).append(")\n");
        sb.append("Currencies: ").append(String.join(", ", currencies)).append("\n");
        sb.append("Update Interval: ").append(FIXED_RATE / 1000).append(" seconds\n");
        sb.append("Per Page: ").append(PER_PAGE).append(" coins\n");
        
        if (isRunning) {
            sb.append("Next Update: ~").append(FIXED_RATE / 1000).append(" seconds\n");
        }
        
        return sb.toString();
    }

    // New methods for max page management
    public boolean setMaxPages(int newMaxPages) {
        if (newMaxPages < MIN_PAGE || newMaxPages > MAX_PAGE_LIMIT) {
            log.warn("Invalid max pages value: {}. Must be between {} and {}", 
                    newMaxPages, MIN_PAGE, MAX_PAGE_LIMIT);
            return false;
        }

        int oldMaxPages = maxPage.get();
        maxPage.set(newMaxPages);
        
        // If current page is now beyond the new max, reset to page 1
        if (currentPage.get() > newMaxPages) {
            currentPage.set(1);
            currentCurrencyIndex.set(0); // Also reset currency to avoid confusion
            log.info("Current page was beyond new max. Reset to USD, page 1");
        }
        
        log.info("Max pages changed from {} to {}", oldMaxPages, newMaxPages);
        return true;
    }

    public int getMaxPages() {
        return maxPage.get();
    }

    public int getMinPages() {
        return MIN_PAGE;
    }

    public int getMaxPageLimit() {
        return MAX_PAGE_LIMIT;
    }

    public String getPageInfo() {
        return String.format("Current: %d/%d, Range: %d-%d", 
                currentPage.get(), maxPage.get(), MIN_PAGE, MAX_PAGE_LIMIT);
    }
}
