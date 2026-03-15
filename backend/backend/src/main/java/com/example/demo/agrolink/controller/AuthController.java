package com.example.demo.agrolink.controller;

import com.example.demo.agrolink.dto.AuthResponse;
import com.example.demo.agrolink.dto.RegisterRequest;
import com.example.demo.agrolink.dto.LoginRequest;
import com.example.demo.agrolink.dto.UsernameAvailabilityResponse;
import com.example.demo.agrolink.model.User;
import com.example.demo.agrolink.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Allows mobile app clients to access auth endpoints
public class AuthController {

    private static final Set<String> ALLOWED_ROLES = Set.of("FARMER", "INVESTOR", "ADMIN");
    private static final Pattern NIC_PATTERN = Pattern.compile("^(\\d{9}[VvXx]|\\d{12})$");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- 1. REGISTER API ---
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        String normalizedUsername = normalizeUsername(request.getUsername());
        String normalizedRole = normalizeRole(request.getRole());
        String normalizedNic = normalizeOptional(request.getNic());

        if (!ALLOWED_ROLES.contains(normalizedRole)) {
            return ResponseEntity.badRequest().body("Invalid role. Allowed roles: FARMER, INVESTOR, ADMIN");
        }

        if ("FARMER".equals(normalizedRole) && normalizedNic == null) {
            return ResponseEntity.badRequest().body("NIC is required for FARMER role");
        }

        if ("FARMER".equals(normalizedRole) && !isValidSriLankanNic(normalizedNic)) {
            return ResponseEntity.badRequest().body("Invalid NIC format for FARMER role");
        }

        // Check if user already exists
        if (userRepository.existsByUsernameIgnoreCase(normalizedUsername)) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // Save the new user
        User newUser = new User(
                normalizedUsername,
                passwordEncoder.encode(request.getPassword()),
            normalizedRole,
            normalizedNic);

        User savedUser = userRepository.save(newUser);
        return ResponseEntity.ok(new AuthResponse(
            "User registered successfully!",
            savedUser.getId(),
            savedUser.getRole(),
            savedUser.getUsername()));
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

                return ResponseEntity.ok(new AuthResponse(
                        "Login Successful",
                        user.getId(),
                        user.getRole(),
                        user.getUsername()));
            } else {
                return ResponseEntity.status(401).body("Invalid Password");
            }
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @GetMapping("/username-availability")
    public ResponseEntity<?> checkUsernameAvailability(@RequestParam String username) {
        String normalizedUsername = normalizeUsername(username);
        if (normalizedUsername == null || normalizedUsername.isBlank()) {
            return ResponseEntity.badRequest().body("username is required");
        }

        boolean exists = userRepository.existsByUsernameIgnoreCase(normalizedUsername);
        return ResponseEntity.ok(new UsernameAvailabilityResponse(normalizedUsername, !exists));
    }

    private String normalizeUsername(String username) {
        return username == null ? null : username.trim().toLowerCase();
    }

    private String normalizeRole(String role) {
        return role == null ? null : role.trim().toUpperCase();
    }

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private boolean isValidSriLankanNic(String nic) {
        return nic != null && NIC_PATTERN.matcher(nic).matches();
    }
}