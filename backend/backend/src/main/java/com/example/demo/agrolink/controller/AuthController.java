package com.example.demo.agrolink.controller;

import com.example.demo.agrolink.dto.RegisterRequest;
import com.example.demo.agrolink.model.User;
import com.example.demo.agrolink.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Allows mobile app clients to access auth endpoints
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // --- 1. REGISTER API ---
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        // Check if user already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // Save the new user
        User newUser = new User(
                request.getUsername(),
                request.getPassword(),
                request.getRole(),
                request.getNic());

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
}