package com.moneyboss.financialtracker.user.debt_user;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.moneyboss.financialtracker.user.UserNotFoundException;
import com.moneyboss.financialtracker.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DebtUserService {

    private final DebtUserRepository debtUserRepository;
    private final UserRepository userRepository;

    public ResponseEntity<List<DebtUserResponse>> getAllDebtsofCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        userRepository.findById(user.getId())
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + user.getId()));
        List <DebtUser> debts = debtUserRepository.findByUserId(user.getId())
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + user.getId()));
        
        List<DebtUserResponse> debtResponses = debts.stream()
            .map(debt -> DebtUserResponse.builder()
                .id(debt.getId())
                .name(debt.getName())
                .category(debt.getCategory())
                .totalAmount(debt.getTotalAmount())
                .monthlyPayment(debt.getMonthlyPayment())
                .interestRate(debt.getInterestRate())
                .insertedAt(debt.getInsertedAt())
                .remainingMonths(debt.getRemainingMonths())
                .build())
            .sorted((d1, d2) -> d1.getInsertedAt().compareTo(d2.getInsertedAt()))
            .toList();

        return ResponseEntity.ok().body(debtResponses);
    }

    public ResponseEntity<DebtUserResponse> createDebt(DebtUserRequest request) {        
        if (request.getName() == null || request.getCategory() == null || request.getTotalAmount() == null ||
            request.getMonthlyPayment() == null || request.getInterestRate() == null || request.getCategory().getName() == null) {
            throw new DebtException("All fields must be provided");
        }
        if (request.getTotalAmount() <= 0 || request.getMonthlyPayment() <= 0 || request.getInterestRate() < 0) {
            throw new DebtException("Total amount and monthly payment must be greater than zero, interest rate cannot be negative");
        }
        if (request.getMonthlyPayment() > request.getTotalAmount()) {
            throw new DebtException("Monthly payment cannot be greater than total amount");
        }
        if (request.getInterestRate() > 100) {
            throw new DebtException("Interest rate cannot be greater than 100%");
        }
        if (request.getName().trim().isEmpty() || request.getCategory().getName().trim().isEmpty()) {
            throw new DebtException("String fields cannot be empty");
        }
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        if(request.getRemainingMonths() == null)
            request.setRemainingMonths((int)(Math.ceil(request.getTotalAmount() / request.getMonthlyPayment())));
        

        DebtUser debt = DebtUser.builder()
            .name(request.getName())
            .category(request.getCategory())
            .totalAmount(request.getTotalAmount())
            .monthlyPayment(request.getMonthlyPayment())
            .interestRate(request.getInterestRate())
            .insertedAt(LocalDateTime.now())
            .remainingMonths(request.getRemainingMonths())
            .user(user)
            .build();
        debtUserRepository.save(debt);

        DebtUserResponse response = DebtUserResponse.builder()
            .id(debt.getId())
            .name(debt.getName())
            .category(debt.getCategory())
            .totalAmount(debt.getTotalAmount())
            .monthlyPayment(debt.getMonthlyPayment())
            .interestRate(debt.getInterestRate())
            .remainingMonths(debt.getRemainingMonths())
            .insertedAt(debt.getInsertedAt())
            .build();
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<DebtUserResponse> deleteDebt(DebtIdRequest request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.findByEmail(username)
            .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        DebtUser debt = debtUserRepository.findById(request.getDebtId())
            .orElseThrow(() -> new DebtException("Debt not found with id: " + request.getDebtId()));

        debtUserRepository.delete(debt);

        DebtUserResponse response = DebtUserResponse.builder()
            .id(debt.getId())
            .name(debt.getName())
            .category(debt.getCategory())
            .totalAmount(debt.getTotalAmount())
            .monthlyPayment(debt.getMonthlyPayment())
            .interestRate(debt.getInterestRate())
            .insertedAt(debt.getInsertedAt())
            .build();
        return ResponseEntity.ok().body(response);
    }

}
