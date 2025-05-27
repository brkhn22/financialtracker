package com.moneyboss.financialtracker.auth.token;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.moneyboss.financialtracker.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public String saveConfirmationToken(User user) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), null, user);
        confirmationTokenRepository.save(confirmationToken);
        return token;
    }

    public String isTokenValidEmail(String token) {
        if (token == null || token.isEmpty())
        throw new InvalidTokenException("Token cannot be null or empty");

        var confirmationToken = confirmationTokenRepository.findByToken(token)
        .orElseThrow(()->(new InvalidTokenException("token not found")));

        if (confirmationToken == null)
            throw new InvalidTokenException("Token not found");
        if (confirmationToken.getConfirmedAt() != null)
            throw new InvalidTokenException("Token already confirmed");
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new InvalidTokenException("Token expired");
        if (confirmationToken.getUser() == null)
            throw new InvalidTokenException("User not found");
        if (confirmationToken.getUser().getActive())
            throw new InvalidTokenException("User already active");

        return confirmationToken.getToken();
    }

    public String isTokenValidPassword(String token) {
        if (token == null || token.isEmpty())
        throw new InvalidTokenException("Token cannot be null or empty");

        var confirmationToken = confirmationTokenRepository.findByToken(token)
        .orElseThrow(()->(new InvalidTokenException("token not found")));

        if (confirmationToken == null)
            throw new InvalidTokenException("Token not found");
        if (confirmationToken.getConfirmedAt() != null)
            throw new InvalidTokenException("Token already confirmed");
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new InvalidTokenException("Token expired");
        if (confirmationToken.getUser() == null)
            throw new InvalidTokenException("User not found");

        return confirmationToken.getToken();
    }

    public User confirmTokenEmail(String token) {
        var confirmationToken = isTokenValidEmailHelper(token);

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken.getUser();
    }

    public User confirmTokenPassword(String token) {
        var confirmationToken = isTokenValidPasswordHelper(token);

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken.getUser();
    }

    private ConfirmationToken isTokenValidEmailHelper(String token) {
        if (token == null || token.isEmpty())
            throw new InvalidTokenException("Token cannot be null or empty");

        var confirmationToken = confirmationTokenRepository.findByToken(token)
        .orElseThrow(()->(new InvalidTokenException("token not found")));

        if (confirmationToken == null)
            throw new InvalidTokenException("Token not found");
        if (confirmationToken.getConfirmedAt() != null)
            throw new InvalidTokenException("Token already confirmed");
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new InvalidTokenException("Token expired");
        if (confirmationToken.getUser() == null)
            throw new InvalidTokenException("User not found");
        if (confirmationToken.getUser().getActive())
            throw new InvalidTokenException("User already active");

        return confirmationToken;
    }

    private ConfirmationToken isTokenValidPasswordHelper(String token) {
        if (token == null || token.isEmpty())
            throw new InvalidTokenException("Token cannot be null or empty");

        var confirmationToken = confirmationTokenRepository.findByToken(token)
        .orElseThrow(()->(new InvalidTokenException("token not found")));

        if (confirmationToken == null)
            throw new InvalidTokenException("Token not found");
        if (confirmationToken.getConfirmedAt() != null)
            throw new InvalidTokenException("Token already confirmed");
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new InvalidTokenException("Token expired");
        if (confirmationToken.getUser() == null)
            throw new InvalidTokenException("User not found");

        return confirmationToken;
    }
}
