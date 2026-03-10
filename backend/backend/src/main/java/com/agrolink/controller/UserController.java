package com.agrolink.controller;

import com.agrolink.model.User;
import com.agrolink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users") // ✅ CRITICAL: This matches the frontend exactly
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 1. Get User Details
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }

        return ResponseEntity.notFound().build();
    }

    // 2. Update Profile Image URL
    @PutMapping("/{id}/profile-image")
    public ResponseEntity<?> updateProfileImage(@PathVariable String id, @RequestBody Map<String, String> request) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Grab the Cloudinary URL sent by the frontend
            String newImageUrl = request.get("imageUrl");

            user.setProfileImageUrl(newImageUrl);
            userRepository.save(user); // Save to MongoDB

            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
}