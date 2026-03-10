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
}