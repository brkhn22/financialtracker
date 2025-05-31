package com.moneyboss.financialtracker.user.debt_user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/debts")
@RequiredArgsConstructor
public class DebtController {

    private final DebtUserService debtUserService;

    @GetMapping("/get-all-debts")
    public ResponseEntity<List<DebtUserResponse>> getAllDebtsofCurrentUser() {
        return debtUserService.getAllDebtsofCurrentUser();
    }

    @PostMapping("/create-debt")
    public ResponseEntity<DebtUserResponse> createDebt(
        @RequestBody DebtUserRequest request) {
        return debtUserService.createDebt(request);
    }

    @PostMapping("/delete-debt-by-id")
    public ResponseEntity<DebtUserResponse> deleteDebt(
        @RequestBody DebtIdRequest request) {
        return debtUserService.deleteDebt(request);
    }
}
