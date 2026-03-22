package com.example.demo.agrolink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsernameAvailabilityResponse {
    private String username;
    private boolean available;
}
