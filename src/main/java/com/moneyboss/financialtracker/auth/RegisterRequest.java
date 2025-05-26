package com.moneyboss.financialtracker.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    
    private String name;
    private String email;
    private String password;
    private String roleName;
    private Boolean enabled = false;
    private Boolean active = false;
}