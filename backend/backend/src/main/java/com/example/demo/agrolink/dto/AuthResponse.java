package com.example.demo.agrolink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String userId;
    private String role;
    private String username;
}
