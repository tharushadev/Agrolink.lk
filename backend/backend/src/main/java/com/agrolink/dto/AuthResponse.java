package com.agrolink.dto;

public class AuthResponse {
    private String userId;
    private String role;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Integer profileStrength;

    public AuthResponse() {}

    public AuthResponse(String userId, String role, String email, String phoneNumber, String firstName, String lastName, Integer profileStrength) {
        this.userId = userId;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileStrength = profileStrength;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getProfileStrength() {
        return profileStrength;
    }

    public void setProfileStrength(Integer profileStrength) {
        this.profileStrength = profileStrength;
    }
}
