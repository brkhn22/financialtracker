package com.moneyboss.financialtracker.auth;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.moneyboss.financialtracker.auth.token.ConfirmRequest;
import com.moneyboss.financialtracker.auth.token.ResendRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
        @RequestBody RegisterRequest request
        ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/activation/resend")
    public ResponseEntity<RegisterResponse> resendEmailToken(
        @RequestBody ResendRequest request
        ) {
        return ResponseEntity.ok(authenticationService.resendEmailToken(request));
    }

    @PostMapping("/activation/confirm")
    public ResponseEntity<AuthenticationResponse> confirm(
        @RequestBody ConfirmRequest request
        ) {
        return ResponseEntity.ok(authenticationService.confirm(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody AuthenticationRequest request
        ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


}