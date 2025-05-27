package com.moneyboss.financialtracker.config;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.moneyboss.financialtracker.auth.EmailAlreadyInUseException;
import com.moneyboss.financialtracker.auth.IllegalRoleException;
import com.moneyboss.financialtracker.auth.token.InvalidTokenException;
import com.moneyboss.financialtracker.item.service.IllegalUpdateItemException;
import com.moneyboss.financialtracker.item.service.ItemNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<?> handleItemNotFound(ItemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    // Add specific authentication/authorization handlers
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "Access denied"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<?> handleEmailAlreadyInUse(EmailAlreadyInUseException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalRoleException.class)
    public ResponseEntity<?> handleIllegalRole(IllegalRoleException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage()));
    }

    // Be more specific about RuntimeException - consider removing this or making it more specific
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        // Log this for debugging since it's unexpected
        ex.printStackTrace(); // Or use proper logging
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An unexpected error occurred"));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidToken(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(IllegalUpdateItemException.class)
    public ResponseEntity<?> handleIllegalUpdateItem(IllegalUpdateItemException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        // Log this for debugging
        ex.printStackTrace(); // Or use proper logging
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An unexpected error occurred"));
    }
}