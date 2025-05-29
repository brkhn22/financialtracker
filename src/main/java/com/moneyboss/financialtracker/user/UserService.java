package com.moneyboss.financialtracker.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResponseEntity<UserSimpleResponse> getUserByEmail(UserEmailRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));
        
        UserSimpleResponse response = UserSimpleResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .active(user.getActive())
                .role(user.getRole())
                .build();
        
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<UserSimpleResponse> getUserById(UserIdRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + request.getId()));
        
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
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + request.getId()));
        
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
