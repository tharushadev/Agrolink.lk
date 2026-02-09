package com.agrolink.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String phoneNumber; // ✅ Changed from username
    private String password;
    private String role;
    private String nic;
    private String profileImage;
    private int profileStrength = 40;

    public User() {}

    public User(String phoneNumber, String password, String role, String nic) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.nic = nic;
    }

    // --- GETTERS & SETTERS ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public int getProfileStrength() { return profileStrength; }
    public void setProfileStrength(int profileStrength) { this.profileStrength = profileStrength; }
}