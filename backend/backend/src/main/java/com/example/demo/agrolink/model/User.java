package com.example.demo.agrolink.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username; // This will be the Email
    private String password; // In a real app, we encrypt this!
    private String role; // "FARMER" or "INVESTOR"
    private String nic; // Only for Farmers

    // Constructors
    public User() {
    }

    public User(String username, String password, String role, String nic) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.nic = nic;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }
}