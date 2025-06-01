package com.moneyboss.financialtracker.config;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/reset")
    public ResponseEntity<String> resetScheduler() {
        schedulerService.resetScheduler();
        return ResponseEntity.ok("Scheduler reset successfully");
    }
}