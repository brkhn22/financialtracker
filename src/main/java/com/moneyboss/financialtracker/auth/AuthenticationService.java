package com.moneyboss.financialtracker.auth;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.moneyboss.financialtracker.auth.token.ConfirmRequest;
import com.moneyboss.financialtracker.auth.token.ConfirmationTokenService;
import com.moneyboss.financialtracker.auth.token.ResendRequest;
import com.moneyboss.financialtracker.config.JwtService;
import com.moneyboss.financialtracker.email.EmailSender;
import com.moneyboss.financialtracker.user.RoleRepository;
import com.moneyboss.financialtracker.user.User;
import com.moneyboss.financialtracker.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final String MAIN_PATH = "https://www.bakiceltik.com.tr";

    public AuthenticationResponse register(RegisterRequest request){
        Validator.isValidEmail(request.getEmail());
        Validator.isValidPassword(request.getPassword());
        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new IllegalArgumentException("Email already in use");
        
        if(request.getRoleName() == null || request.getRoleName().isEmpty())
            throw new IllegalArgumentException("Role name cannot be empty");
        var role = roleRepository.findByName(request.getRoleName()).orElseThrow();
        
        var user = User.builder()
        .name(request.getName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(role)
        .active(false)
        .enabled(false)
        .build();

        userRepository.save(user);
        String token = confirmationTokenService.saveConfirmationToken(user);
        String link = MAIN_PATH+"?token="+token;
        String message = "<p>Click the link to activate your account:</p>" +
        "<a href=\"" + link + "\">" + link + "</a>";
        emailSender.send(request.getEmail(), message);

        return AuthenticationResponse.builder()
        .token(token)
        .build();

    }

    public AuthenticationResponse resendEmailToken(ResendRequest request){
        Validator.isValidEmail(request.getEmail());

        var user = userRepository.findByEmail(request.getEmail())
        .orElseThrow();
        
        if(user.getActive())
            throw new IllegalArgumentException("User already active");

        String token = confirmationTokenService.saveConfirmationToken(user);
        String link = MAIN_PATH+"?token="+token;
        String message = "<p>Click the link to activate your account:</p>" +
        "<a href=\"" + link + "\">" + link + "</a>";
        emailSender.send(request.getEmail(), message);
        return AuthenticationResponse.builder()
        .token(token)
        .build();
    }

    public AuthenticationResponse confirm(ConfirmRequest request){
        var user = confirmationTokenService.confirmTokenEmail(request.getToken());

        user.setActive(true);
        user.setEnabled(true);
        userRepository.save(user);

        // authentication token
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        Validator.isValidEmail(request.getEmail());
        Validator.isValidPassword(request.getPassword());
        
        authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
            )
        );

        var user = userRepository.findByEmail(request.getEmail())
        .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
    }
}