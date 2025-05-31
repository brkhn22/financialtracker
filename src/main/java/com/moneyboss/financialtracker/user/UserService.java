package com.moneyboss.financialtracker.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.moneyboss.financialtracker.user.user_finance.SimpleUserFinanceResponse;
import com.moneyboss.financialtracker.user.user_finance.UpdateUserFinanceRequest;
import com.moneyboss.financialtracker.user.user_finance.UserFinance;
import com.moneyboss.financialtracker.user.user_finance.UserFinanceException;
import com.moneyboss.financialtracker.user.user_finance.UserFinanceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserFinanceRepository userFinanceRepository;

    public ResponseEntity<UserSimpleResponse> getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        
        UserSimpleResponse response = UserSimpleResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .active(user.getActive())
                .role(user.getRole())
                .build();
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<SimpleUserFinanceResponse> getCurrentUserFinance() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        UserFinance userFinance = userFinanceRepository.findByUserId(user.getId())
                .orElseThrow(() -> new UserFinanceException("Finance details not found for user: " + username));
        
        SimpleUserFinanceResponse response = SimpleUserFinanceResponse.builder()
                .userId(user.getId())
                .monthlyIncome(userFinance.getMonthlyIncome())
                .currentSaving(userFinance.getCurrentSaving())
                .investmentGoal(userFinance.getInvestmentGoal())
                .build();
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<SimpleUserFinanceResponse> updateCurrentUserFinance(UpdateUserFinanceRequest request) {
        if(request.getCurrentSaving() == null && request.getInvestmentGoal() == null && request.getMonthlyIncome() == null) 
            throw new UserFinanceException("At least one field must be provided for update");
        
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        
        UserFinance userFinance = userFinanceRepository.findByUserId(user.getId())
                .orElseThrow(() -> new UserFinanceException("Finance details not found for user: " + username));
        

        if (request.getMonthlyIncome() != null && 
        request.getMonthlyIncome() >= 0 && 
        request.getMonthlyIncome() != userFinance.getMonthlyIncome()) 
                userFinance.setMonthlyIncome(request.getMonthlyIncome());

        if (request.getCurrentSaving() != null && 
        request.getCurrentSaving() >= 0 && 
        request.getCurrentSaving() != userFinance.getCurrentSaving())
                userFinance.setCurrentSaving(request.getCurrentSaving());

        if (request.getInvestmentGoal() != null && 
        request.getInvestmentGoal() >= 0 && 
        request.getInvestmentGoal() != userFinance.getInvestmentGoal())
                userFinance.setInvestmentGoal(request.getInvestmentGoal());
        
        userFinanceRepository.save(userFinance);
        
        SimpleUserFinanceResponse response = SimpleUserFinanceResponse.builder()
                .userId(user.getId())
                .monthlyIncome(userFinance.getMonthlyIncome())
                .currentSaving(userFinance.getCurrentSaving())
                .investmentGoal(userFinance.getInvestmentGoal())
                .build();
        
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<UserSimpleResponse> getUserById(UserIdRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + request.getUserId()));
        
        UserSimpleResponse response = UserSimpleResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .active(user.getActive())
                .role(user.getRole())
                .build();
        
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<UsersSimpleResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found");
        }
        
        return ResponseEntity.ok().body(UsersSimpleResponse.builder()
                .users(users.stream()
                        .map(user -> UserSimpleResponse.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .name(user.getName())
                                .active(user.getActive())
                                .role(user.getRole())
                                .build())
                        .toList())
                .build());
    }

    public ResponseEntity<UserSimpleResponse> deleteUser(UserIdRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + request.getUserId()));
        
        userRepository.delete(user);
        
        UserSimpleResponse response = UserSimpleResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .active(user.getActive())
                .role(user.getRole())
                .build();
        return ResponseEntity.ok().body(response);
    }
}
