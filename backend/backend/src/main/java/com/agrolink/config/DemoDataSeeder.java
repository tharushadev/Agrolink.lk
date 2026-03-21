package com.agrolink.config;

import com.agrolink.config.DemoUsersProperties.DemoUser;
import com.agrolink.model.User;
import com.agrolink.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Component
public class DemoDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);

    @Value("${app.seed-demo-users:true}")
    private boolean seedDemoUsers;

    @Value("${app.seed-demo-users.force:false}")
    private boolean forceOverwrite;

    @Autowired
    private DemoUsersProperties demoUsersProperties;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String normalizePhone(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        String trimmed = phoneNumber.trim();
        if (trimmed.isEmpty()) {
            return "";
        }
        String digits = trimmed.replaceAll("\\D", "");
        if (digits.isEmpty()) {
            return "";
        }
        if (digits.startsWith("94") && digits.length() == 11) {
            digits = "0" + digits.substring(2);
        }
        if (digits.length() == 9) {
            digits = "0" + digits;
        }
        return digits;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!seedDemoUsers) {
            log.info("Demo user seeding disabled (app.seed-demo-users=false)");
            return;
        }

        log.info("Seeding demo users (forceOverwrite={})", forceOverwrite);

        if (demoUsersProperties.getUsers() == null || demoUsersProperties.getUsers().isEmpty()) {
            log.warn("No demo users configured under app.demo-users.users[]. Skipping seeding.");
            return;
        }

        for (DemoUser demoUser : demoUsersProperties.getUsers()) {
            seedUser(demoUser);
        }
    }

    private void seedUser(DemoUser demoUser) {
        if (demoUser == null) {
            return;
        }
        if (demoUser.getPassword() == null || demoUser.getPassword().trim().isEmpty()) {
            log.warn("Skipping demo user with missing password");
            return;
        }
        if (demoUser.getRole() == null || demoUser.getRole().trim().isEmpty()) {
            log.warn("Skipping demo user with missing role");
            return;
        }
        if (demoUser.getNic() == null || demoUser.getNic().trim().isEmpty()) {
            log.warn("Skipping demo user with missing NIC");
            return;
        }
        if (demoUser.getPhoneNumber() == null || demoUser.getPhoneNumber().trim().isEmpty()) {
            log.warn("Skipping demo user with missing phoneNumber");
            return;
        }

        String normalizedPhone = normalizePhone(demoUser.getPhoneNumber());
        if (normalizedPhone == null || normalizedPhone.isEmpty()) {
            log.warn("Skipping demo user with invalid phoneNumber");
            return;
        }

        String normalizedRole = demoUser.getRole().trim().toUpperCase(Locale.ROOT);

        Optional<User> existing = userRepository.findByPhoneNumber(normalizedPhone);
        if (existing.isPresent() && !forceOverwrite) {
            log.info("Demo user already exists; skipping: phone={}, role={}", maskPhone(normalizedPhone), normalizedRole);
            return;
        }

        boolean isUpdate = existing.isPresent();

        User user = existing.orElseGet(User::new);
        user.setRole(normalizedRole);
        user.setPassword(passwordEncoder.encode(demoUser.getPassword().trim()));

        user.setNic(demoUser.getNic().trim());
        user.setPhoneNumber(normalizedPhone);
        user.setFirstName(demoUser.getFirstName());
        user.setLastName(demoUser.getLastName());

        if (demoUser.getProfileImageUrl() != null) {
            user.setProfileImageUrl(demoUser.getProfileImageUrl());
        }
        if (demoUser.getGnCertificateUrl() != null) {
            user.setGnCertificateUrl(demoUser.getGnCertificateUrl());
        }

        userRepository.save(user);

        if (isUpdate) {
            log.info("Demo user updated: phone={}, role={}", maskPhone(normalizedPhone), normalizedRole);
        } else {
            log.info("Demo user created: phone={}, role={}", maskPhone(normalizedPhone), normalizedRole);
        }
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return "";
        }
        int keep = Math.min(3, phone.length());
        return "***" + phone.substring(phone.length() - keep);
    }
}
