package com.agrolink.controller;

import com.agrolink.dto.AuthLoginRequest;
import com.agrolink.dto.AuthResponse;
import com.agrolink.dto.AuthSignupRequest;
import com.agrolink.model.User;
import com.agrolink.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.data.mongodb.uri:}")
    private String mongoUri;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody AuthSignupRequest request) {
        String normalizedRole = request.getRole().trim().toUpperCase(java.util.Locale.ROOT);
        String normalizedNic = normalizeNic(request.getNic());
        String normalizedPhone = normalizePhone(request.getPhoneNumber());

        if (!normalizedRole.equals("FARMER") && !normalizedRole.equals("INVESTOR")) {
            log.info("Signup rejected: invalid role={} phone={}", normalizedRole, maskPhone(normalizedPhone));
            return ResponseEntity.badRequest().body("Invalid role. Use FARMER or INVESTOR.");
        }

        if (normalizedPhone == null || normalizedPhone.isEmpty()) {
            log.info("Signup rejected: missing phone role={}", normalizedRole);
            return ResponseEntity.badRequest().body("Phone number is required");
        }

        // NIC is required for both FARMER and INVESTOR signups
        if (normalizedNic == null || normalizedNic.isEmpty()) {
            log.info("Signup rejected: missing NIC role={} phone={}", normalizedRole, maskPhone(normalizedPhone));
            return ResponseEntity.badRequest().body("NIC is required");
        }
        if (userRepository.existsByNicIgnoreCase(normalizedNic)) {
            log.info(
                    "Signup rejected: NIC exists role={} phone={} usersCount={} mongoUri={}",
                    normalizedRole,
                    maskPhone(normalizedPhone),
                    userRepository.count(),
                    sanitizeMongoUri(mongoUri)
            );
            return ResponseEntity.badRequest().body("NIC is already registered");
        }

        if (userRepository.existsByPhoneNumber(normalizedPhone)) {
            log.info("Signup rejected: phone exists role={} phone={}", normalizedRole, maskPhone(normalizedPhone));
            return ResponseEntity.badRequest().body("Phone number is already registered");
        }

        if (normalizedRole.equals("FARMER")) {
            if (request.getFarmerPhotoUrl() == null || request.getFarmerPhotoUrl().trim().isEmpty()) {
                log.info("Signup rejected: missing farmer photo role={} phone={}", normalizedRole, maskPhone(normalizedPhone));
                return ResponseEntity.badRequest().body("Farmer photo is required");
            }
            if (request.getGnCertificateUrl() == null || request.getGnCertificateUrl().trim().isEmpty()) {
                log.info("Signup rejected: missing GN certificate role={} phone={}", normalizedRole, maskPhone(normalizedPhone));
                return ResponseEntity.badRequest().body("Grama Sevaka certificate (PDF) is required");
            }
        }

        User user = new User();
        user.setRole(normalizedRole);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(normalizedPhone);
        user.setNic(normalizedNic);

        // Reuse existing URL fields on the main User model
        if (request.getFarmerPhotoUrl() != null && !request.getFarmerPhotoUrl().trim().isEmpty()) {
            user.setProfileImageUrl(request.getFarmerPhotoUrl().trim());
        }
        if (request.getGnCertificateUrl() != null && !request.getGnCertificateUrl().trim().isEmpty()) {
            user.setGnCertificateUrl(request.getGnCertificateUrl().trim());
        }

        User saved = userRepository.save(user);
        log.info("Signup successful: role={} phone={}", normalizedRole, maskPhone(normalizedPhone));
        return ResponseEntity.ok(toAuthResponse(saved));
    }

    // Backward-compatible alias for older frontend clients
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthSignupRequest request) {
        return signup(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthLoginRequest request) {
        String normalizedPhone = normalizePhone(request.getPhoneNumber());

        if (normalizedPhone == null || normalizedPhone.isEmpty()) {
            return ResponseEntity.badRequest().body("phoneNumber is required");
        }

        Optional<User> userOpt = userRepository.findByPhoneNumber(normalizedPhone);
        String invalidCredentialsMessage = "Invalid phone number or password";

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(invalidCredentialsMessage);
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(invalidCredentialsMessage);
        }

        if (request.getRole() != null && !request.getRole().trim().isEmpty()) {
            String role = request.getRole().trim().toUpperCase(java.util.Locale.ROOT);
            if (user.getRole() != null && !role.equalsIgnoreCase(user.getRole())) {
                return ResponseEntity.status(403).body("Selected role does not match account role");
            }
        }

        return ResponseEntity.ok(toAuthResponse(user));
    }

    private String normalizePhone(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        String trimmed = phoneNumber.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        // Normalize to digits only
        String digits = trimmed.replaceAll("\\D", "");
        if (digits.isEmpty()) {
            return "";
        }

        // Sri Lanka common formats:
        // +94XXXXXXXXX -> 0XXXXXXXXX
        // 94XXXXXXXXX  -> 0XXXXXXXXX
        if (digits.startsWith("94") && digits.length() == 11) {
            digits = "0" + digits.substring(2);
        }

        // If user typed 9 digits without leading 0, add it.
        if (digits.length() == 9) {
            digits = "0" + digits;
        }

        return digits;
    }

    private String normalizeNic(String nic) {
        if (nic == null) {
            return null;
        }
        String trimmed = nic.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        // NIC should not contain whitespace; normalize common variants.
        String compact = trimmed.replaceAll("\\s+", "");
        return compact.toUpperCase(java.util.Locale.ROOT);
    }

    private String sanitizeMongoUri(String uri) {
        if (uri == null || uri.isBlank()) {
            return "";
        }

        int schemeIdx = uri.indexOf("://");
        if (schemeIdx < 0) {
            return uri;
        }

        int credsStart = schemeIdx + 3;
        int atIdx = uri.indexOf('@', credsStart);
        if (atIdx < 0) {
            return uri;
        }

        // Replace anything between scheme and '@' with masked credentials.
        return uri.substring(0, credsStart) + "***:***@" + uri.substring(atIdx + 1);
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return "";
        }
        String digits = phone;
        int keep = Math.min(3, digits.length());
        return "***" + digits.substring(digits.length() - keep);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        // Simulated for mobile MVP: do not reveal whether phone number exists.
        return ResponseEntity.ok(Map.of(
            "message", "If the phone number exists, a reset code will be sent.")
        );
    }

    private AuthResponse toAuthResponse(User user) {
        return new AuthResponse(
                user.getId(),
                user.getRole(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfileStrength()
        );
    }
}
