package com.moneyboss.financialtracker.config;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moneyboss.financialtracker.coin.CoinUpdateSchedulerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/scheduler")
@RequiredArgsConstructor
public class SchedulerController {

    private final CoinUpdateSchedulerService schedulerService;

    @GetMapping("/status")
    public ResponseEntity<String> getSchedulerStatus() {
        return ResponseEntity.ok(schedulerService.getCurrentStatus());
    }

    @GetMapping("/detailed-status")
    public ResponseEntity<String> getDetailedStatus() {
        return ResponseEntity.ok(schedulerService.getDetailedStatus());
    }

    @PostMapping("/start")
    public ResponseEntity<String> startScheduler() {
        if (schedulerService.startScheduler()) {
            return ResponseEntity.ok("Scheduler started successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to start scheduler (might already be running)");
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopScheduler() {
        if (schedulerService.stopScheduler()) {
            return ResponseEntity.ok("Scheduler stopped successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to stop scheduler (might not be running)");
        }
    }

    @PostMapping("/restart")
    public ResponseEntity<String> restartScheduler() {
        if (schedulerService.restartScheduler()) {
            return ResponseEntity.ok("Scheduler restarted successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to restart scheduler");
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetScheduler() {
        schedulerService.resetScheduler();
        return ResponseEntity.ok("Scheduler reset successfully (position reset to USD, page 1)");
    }

    @GetMapping("/is-running")
    public ResponseEntity<Boolean> isSchedulerRunning() {
        return ResponseEntity.ok(schedulerService.isSchedulerRunning());
    }

    // New endpoints for max pages management
    @GetMapping("/max-pages")
    public ResponseEntity<Integer> getMaxPages() {
        return ResponseEntity.ok(schedulerService.getMaxPages());
    }

    @GetMapping("/page-info")
    public ResponseEntity<String> getPageInfo() {
        return ResponseEntity.ok(schedulerService.getPageInfo());
    }

    @PostMapping("/set-max-pages")
    public ResponseEntity<String> setMaxPages(@RequestBody MaxPagesRequest request) {
        if (request.getMaxPages() == null) {
            return ResponseEntity.badRequest().body("Max pages value is required");
        }

        if (schedulerService.setMaxPages(request.getMaxPages())) {
            return ResponseEntity.ok(String.format(
                "Max pages set to %d successfully. Current page info: %s", 
                request.getMaxPages(), 
                schedulerService.getPageInfo()
            ));
        } else {
            return ResponseEntity.badRequest().body(String.format(
                "Invalid max pages value: %d. Must be between %d and %d", 
                request.getMaxPages(), 
                schedulerService.getMinPages(), 
                schedulerService.getMaxPageLimit()
            ));
        }
    }

    // Inner class for request body
    public static class MaxPagesRequest {
        private Integer maxPages;

        public MaxPagesRequest() {}

        public MaxPagesRequest(Integer maxPages) {
            this.maxPages = maxPages;
        }

        public Integer getMaxPages() {
            return maxPages;
        }

        public void setMaxPages(Integer maxPages) {
            this.maxPages = maxPages;
        }
    }
}