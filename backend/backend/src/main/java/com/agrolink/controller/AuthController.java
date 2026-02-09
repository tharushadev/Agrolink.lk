package com.agrolink.controller;

import com.agrolink.model.User;
import com.agrolink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // ✅ Use Spring's BCrypt
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // ✅ Create the encoder
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // --- REGISTER ---
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {

        // 1. Check if Phone Number exists
        if (userRepository.findByPhoneNumber(newUser.getPhoneNumber()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Phone Number is already registered!");
        }

        // 2. 🔒 ENCRYPT THE PASSWORD
        String rawPassword = newUser.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);
        newUser.setPassword(hashedPassword);

        User savedUser = userRepository.save(newUser);

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully!",
                "userId", savedUser.getId(),
                "role", savedUser.getRole()
        ));
    }

    // --- LOGIN ---
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
        String phoneNumber = loginData.get("phoneNumber");
        String rawPassword = loginData.get("password");

        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // 3. 🔒 CHECK MATCH
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return ResponseEntity.ok(Map.of(
                        "message", "Login Successful",
                        "userId", user.getId(),
                        "role", user.getRole(),
                        "phoneNumber", user.getPhoneNumber(),
                        "profileImage", (user.getProfileImage() != null) ? user.getProfileImage() : ""
                ));
            } else {
                return ResponseEntity.status(401).body("Invalid Password");
            }
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}