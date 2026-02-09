package com.agrolink.controller;

import com.agrolink.model.User;
import com.agrolink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{userId}/upload-image")
    public ResponseEntity<?> uploadImage(@PathVariable String userId, @RequestBody Map<String, String> payload) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // 1. Save the image string
            user.setProfileImage(payload.get("image"));

            // 2. Increase Profile Strength (Logic: Add 10% for photo)
            if (user.getProfileStrength() < 50) {
                user.setProfileStrength(50);
            }

            userRepository.save(user);

            return ResponseEntity.ok(Map.of(
                    "message", "Image uploaded successfully",
                    "newStrength", user.getProfileStrength()
            ));
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}