package com.example.demo.agrolink.controller;

import com.example.demo.agrolink.dto.StoredFarmerDocument;
import com.example.demo.agrolink.model.User;
import com.example.demo.agrolink.repository.UserRepository;
import com.example.demo.agrolink.service.FarmerDocumentStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Allow phone to access
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerDocumentStorageService farmerDocumentStorageService;

    // --- 1. REGISTER API ---
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
        // Check if user already exists
        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // Save the new user
        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully!");
    }

    // --- 2. LOGIN API ---
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Simple password check (In production, use BCrypt!)
            if (user.getPassword().equals(password)) {
                // Login Success! Return the user data (excluding password)
                return ResponseEntity.ok(Map.of(
                        "message", "Login Successful",
                        "userId", user.getId(),
                        "role", user.getRole(),
                        "username", user.getUsername()));
            } else {
                return ResponseEntity.status(401).body("Invalid Password");
            }
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    private User buildFarmerUser(String username, String password, String nic) {
        User farmer = new User();
        farmer.setUsername(username);
        farmer.setPassword(password);
        farmer.setRole("FARMER");
        farmer.setNic(nic);
        return farmer;
    }

    private Map<String, Object> buildFarmerSignupResponse(User farmer) {
        return Map.of(
                "message", "Farmer registered successfully!",
                "userId", farmer.getId(),
                "role", farmer.getRole(),
                "username", farmer.getUsername(),
                "documentCount", farmer.getFarmerDocumentPaths() == null ? 0 : farmer.getFarmerDocumentPaths().size());
    }
}