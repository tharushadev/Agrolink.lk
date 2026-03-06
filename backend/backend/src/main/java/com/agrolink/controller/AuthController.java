package com.agrolink.controller;

import com.agrolink.model.User;
import com.agrolink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // --- REGISTER ---
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {

        // 1. Check if Phone Number already exists in the database
        if (userRepository.findByPhoneNumber(newUser.getPhoneNumber()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Phone Number is already registered!");
        }

        // 2. Encrypt the password before saving
        String rawPassword = newUser.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);
        newUser.setPassword(hashedPassword);

        // 3. Save user
        User savedUser = userRepository.save(newUser);

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully!",
                "userId", savedUser.getId(),
                "role", savedUser.getRole(),
                "firstName", savedUser.getFirstName(),
                "lastName", savedUser.getLastName()
        ));
    }

    // --- LOGIN ---
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
        // We only expect phoneNumber and password from the frontend now
        String phoneNumber = loginData.get("phoneNumber");
        String rawPassword = loginData.get("password");

        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // 🔒 Check Encrypted Password
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return ResponseEntity.ok(Map.of(
                        "message", "Login Successful",
                        "userId", user.getId(),
                        "role", user.getRole(),
                        "phoneNumber", user.getPhoneNumber(),
                        "firstName", user.getFirstName() != null ? user.getFirstName() : "",
                        "lastName", user.getLastName() != null ? user.getLastName() : "",
                        "profileImage", user.getProfileImage() != null ? user.getProfileImage() : ""
                ));
            } else {
                return ResponseEntity.status(401).body("Invalid Password");
            }
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}