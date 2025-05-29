package com.moneyboss.financialtracker.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/get-by-email")
    public ResponseEntity<UserSimpleResponse> getUserByEmail(UserEmailRequest request) {
        return userService.getUserByEmail(request);
    }

    @PostMapping("/get-by-id")
    public ResponseEntity<UserSimpleResponse> getUserById(UserIdRequest request) {
        return userService.getUserById(request);
    }

    @PostMapping("/delete")
    public ResponseEntity<UserSimpleResponse> deleteUser(UserIdRequest request) {
        return userService.deleteUser(request);
    }

    @GetMapping("/get-all")
    public ResponseEntity<UsersSimpleResponse> getAllUsers() {
        return userService.getAllUsers();
    }
    
}
