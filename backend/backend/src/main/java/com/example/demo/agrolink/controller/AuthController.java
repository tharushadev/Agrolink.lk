package com.example.demo.agrolink.controller;

import com.example.demo.agrolink.dto.RegisterRequest;
import com.example.demo.agrolink.dto.LoginRequest;
import com.example.demo.agrolink.model.User;
import com.example.demo.agrolink.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Allows mobile app clients to access auth endpoints
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- 1. REGISTER API ---
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        String normalizedUsername = normalizeUsername(request.getUsername());

        // Check if user already exists
        if (userRepository.findByUsernameIgnoreCase(normalizedUsername).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // Save the new user
        User newUser = new User(
                normalizedUsername,
                passwordEncoder.encode(request.getPassword()),
                request.getRole(),
                request.getNic());

        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully!");
    }

    // --- 2. LOGIN API ---
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
        String username = normalizeUsername(request.getUsername());
        String password = request.getPassword();

        Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean passwordMatches = passwordEncoder.matches(password, user.getPassword())
                    || user.getPassword().equals(password);

            if (passwordMatches) {
                if (user.getPassword().equals(password)) {
                    user.setPassword(passwordEncoder.encode(password));
                    userRepository.save(user);
                }

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

    private String normalizeUsername(String username) {
        return username == null ? null : username.trim().toLowerCase();
    }
}