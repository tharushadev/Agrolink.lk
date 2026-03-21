package com.agrolink.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthLoginRequest {
    // Preferred login identifier for mobile
    @NotBlank(message = "phoneNumber is required")
    @JsonAlias({"phone", "mobile", "mobileNumber"})
    private String phoneNumber;

    @NotBlank
    private String password;

    // Optional: frontend role toggle can send this to enforce role selection.
    private String role;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
