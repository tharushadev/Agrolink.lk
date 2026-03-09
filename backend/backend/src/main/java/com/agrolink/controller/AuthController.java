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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {

        // 1. Check if phone number is already used
        if (userRepository.findByPhoneNumber(newUser.getPhoneNumber()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Phone Number is already registered!");
        }

        // 2. Encrypt Password
        String rawPassword = newUser.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);
        newUser.setPassword(hashedPassword);

        // 3. ✅ SMART DATA HANDLING BASED ON ROLE
        if ("FARMER".equalsIgnoreCase(newUser.getRole())) {
            // Set default farmer values if they aren't provided by the frontend
            newUser.setIsVerified(false); // Requires admin approval later
            if (newUser.getYearsOfExperience() == null) newUser.setYearsOfExperience(0);
            if (newUser.getNumberOfProjects() == null) newUser.setNumberOfProjects(0);
            if (newUser.getSuccessfulProjects() == null) newUser.setSuccessfulProjects(0);
        } else {
            // If it is an INVESTOR, ensure farmer fields are kept completely empty
            newUser.setYearsOfExperience(null);
            newUser.setNumberOfProjects(null);
            newUser.setSuccessfulProjects(null);
            newUser.setIsVerified(null);
            newUser.setGnCertificateUrl(null);
            newUser.setLatitude(null);
            newUser.setLongitude(null);
            newUser.setCity(null);
        }

        // 4. Save to Database
        User savedUser = userRepository.save(newUser);

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully!",
                "userId", savedUser.getId(),
                "role", savedUser.getRole()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
        String phoneNumber = loginData.get("phoneNumber");
        String rawPassword = loginData.get("password");

        Optional<User> userOpt = userRepository.findByPhoneNumber(phoneNumber);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return ResponseEntity.ok(Map.of(
                        "message", "Login Successful",
                        "userId", user.getId(),
                        "role", user.getRole(),
                        "firstName", user.getFirstName() != null ? user.getFirstName() : "",
                        "lastName", user.getLastName() != null ? user.getLastName() : "",
                        "isVerified", user.getIsVerified() != null ? user.getIsVerified() : false
                ));
            } else {
                return ResponseEntity.status(401).body("Invalid Password");
            }
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}