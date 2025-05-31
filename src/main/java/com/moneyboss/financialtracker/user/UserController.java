package com.moneyboss.financialtracker.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moneyboss.financialtracker.user.user_finance.SimpleUserFinanceResponse;
import com.moneyboss.financialtracker.user.user_finance.UpdateUserFinanceRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get-current-user")
    public ResponseEntity<UserSimpleResponse> getCurrentUser() {
        return userService.getCurrentUser();
    }
    
    @PostMapping("/get-by-id")
    public ResponseEntity<UserSimpleResponse> getUserById(
        @RequestBody UserIdRequest request) {
        return userService.getUserById(request);
    }

    @PostMapping("/delete")
    public ResponseEntity<UserSimpleResponse> deleteUser(
        @RequestBody UserIdRequest request) {
        return userService.deleteUser(request);
    }

    @GetMapping("/get-all")
    public ResponseEntity<UsersSimpleResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/get-current-user-finance")
    public ResponseEntity<SimpleUserFinanceResponse> getCurrentUserFinance() {
        return userService.getCurrentUserFinance();
    }

    @PostMapping("/update-current-user-finance")
    public ResponseEntity<SimpleUserFinanceResponse> updateCurrentUserFinance(
        @RequestBody UpdateUserFinanceRequest request) {
        return userService.updateCurrentUserFinance(request);
    }
    
}
