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

    public RegisterResponse register(RegisterRequest request){
        Validator.isValidEmail(request.getEmail());
        Validator.isValidPassword(request.getPassword());
        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new EmailAlreadyInUseException("Email already in use");
        
        if(request.getRoleName() == null || request.getRoleName().isEmpty())
            throw new IllegalRoleException("Role name cannot be empty");
        var role = roleRepository.findByName("user").orElseThrow();
        
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
        String link = MAIN_PATH + "?token=" + token;

        String message = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                <h2 style="color: #333333;">Activate Your Account</h2>
                <p style="font-size: 16px; color: #555555;">
                    Thank you for signing up! Please click the button below to activate your account:
                </p>
                <p style="text-align: center; margin: 30px 0;">
                    <a href="%s" style="background-color: #4CAF50; color: white; padding: 12px 20px; text-decoration: none; border-radius: 5px; font-size: 16px;">
                    Activate Account
                    </a>
                </p>
                <p style="font-size: 14px; color: #999999;">
                    If the button doesn't work, you can also copy and paste the link below into your browser:
                    <br><a href="%s">%s</a>
                </p>
                </div>
            </body>
            </html>
        """.formatted(link, link, link);
        // send email
        emailSender.send(request.getEmail(), message);

        return RegisterResponse.builder()
        .emailToken(token)
        .build();

    }

    public RegisterResponse resendEmailToken(ResendRequest request){
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
        return RegisterResponse.builder()
        .emailToken(token)
        .build();
    }

    public AuthenticationResponse confirm(ConfirmRequest request){
        var user = confirmationTokenService.confirmTokenEmail(request.getEmailToken());

        user.setActive(true);
        user.setEnabled(true);
        userRepository.save(user);

        // authentication token
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
        .token(jwtToken)
        .user(user)
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
        .user(user)
        .build();
    }
}