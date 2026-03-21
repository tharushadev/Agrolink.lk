package com.agrolink.controller;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.agrolink.model.User;
import com.agrolink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/users") // ✅ CRITICAL: This matches the frontend exactly
@CrossOrigin(origins = "*")
public class UserController {

    private static final Pattern BASIC_EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // ✅ Added to safely handle passwords

    // 1. Get User Details
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }

        return ResponseEntity.notFound().build();
    }

        // 1b. Trust Score breakdown (used by Farmer Profile -> Trust Score screen)
        @GetMapping("/{id}/trust-score")
        public ResponseEntity<?> getTrustScore(@PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        List<Map<String, Object>> breakdown = new ArrayList<>();

        breakdown.add(Map.of(
            "key", "nic",
            "label", "NIC Provided",
            "points", (user.getNic() != null && !user.getNic().trim().isEmpty()) ? 10 : 0
        ));
        breakdown.add(Map.of(
            "key", "gnCertificate",
            "label", "Grama Sevaka Certificate",
            "points", (user.getGnCertificateUrl() != null && !user.getGnCertificateUrl().trim().isEmpty()) ? 10 : 0
        ));
        breakdown.add(Map.of(
            "key", "profilePhoto",
            "label", "Profile Photo",
            "points", (user.getProfileImageUrl() != null && !user.getProfileImageUrl().trim().isEmpty()) ? 5 : 0
        ));
        breakdown.add(Map.of(
            "key", "address",
            "label", "Address Added",
            "points", (user.getAddress() != null && !user.getAddress().trim().isEmpty()) ? 5 : 0
        ));
        breakdown.add(Map.of(
            "key", "skills",
            "label", "Skills Added",
            "points", (user.getSkills() != null) ? Math.min(user.getSkills().size(), 5) : 0
        ));

        return ResponseEntity.ok(Map.of(
            "userId", user.getId(),
            "trustScore", user.getProfileStrength(),
            "breakdown", breakdown
        ));
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
    // 3. Update General Profile Details & Calculate Trust Score
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable String id, @RequestBody User updatedData) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int strengthBoost = 0;

            if (updatedData.getFirstName() != null) user.setFirstName(updatedData.getFirstName());
            if (updatedData.getLastName() != null) user.setLastName(updatedData.getLastName());
            if (updatedData.getNic() != null) user.setNic(updatedData.getNic());

            // ✅ Optional: user can add email after login (NOT required at registration)
            if (updatedData.getEmail() != null) {
                String email = updatedData.getEmail().trim();
                if (!email.isEmpty()) {
                    String normalizedEmail = email.toLowerCase(Locale.ROOT);
                    if (!BASIC_EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
                        return ResponseEntity.badRequest().body("Invalid email format");
                    }

                    Optional<User> existing = userRepository.findByEmailIgnoreCase(normalizedEmail);
                    if (existing.isPresent() && existing.get().getId() != null && !existing.get().getId().equals(user.getId())) {
                        return ResponseEntity.badRequest().body("Email is already registered");
                    }

                    user.setEmail(normalizedEmail);
                }
            }

            // ✅ Address Gamification: +5 points if added for the first time
            if (updatedData.getAddress() != null) {
                boolean wasAddressEmpty = (user.getAddress() == null || user.getAddress().trim().isEmpty());
                boolean isNewAddressValid = !updatedData.getAddress().trim().isEmpty();

                if (wasAddressEmpty && isNewAddressValid) {
                    strengthBoost += 5;
                }
                user.setAddress(updatedData.getAddress());
            }

            // ✅ Skills Gamification: +1 point per NEW skill added
            if (updatedData.getSkills() != null) {
                int oldSkillsCount = (user.getSkills() != null) ? user.getSkills().size() : 0;
                int newSkillsCount = updatedData.getSkills().size();

                if (newSkillsCount > oldSkillsCount) {
                    strengthBoost += (newSkillsCount - oldSkillsCount);
                }
                user.setSkills(updatedData.getSkills());
            }

            // ✅ Apply the boost directly to the database property
            if (strengthBoost > 0) {
                user.setProfileStrength(user.getProfileStrength() + strengthBoost);
            }

            userRepository.save(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    // 4. Update Password
    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable String id, @RequestBody Map<String, String> request) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String currentPassword = request.get("currentPassword");
            String newPassword = request.get("newPassword");

            // Check if the current password matches the one in the DB
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                return ResponseEntity.status(400).body("Invalid current password");
            }

            // Encode and save the new password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            return ResponseEntity.ok("Password updated successfully");
        }
        return ResponseEntity.notFound().build();
    }

}