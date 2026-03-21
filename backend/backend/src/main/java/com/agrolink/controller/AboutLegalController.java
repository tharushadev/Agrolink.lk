package com.agrolink.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profile/about")
@CrossOrigin(origins = "*")
public class AboutLegalController {

    @GetMapping("/privacy")
    public ResponseEntity<?> getPrivacyPolicy() {
        return ResponseEntity.ok(Map.of(
                "title", "Privacy Policy (Summary)",
                "lastUpdated", "2026-03-21",
                "body", "This is a short, non-legal summary for the in-app modal. AgroLink collects basic account and project data to operate the service. We aim to follow Sri Lanka's Personal Data Protection Act (PDPA) principles such as purpose limitation, data minimization, security safeguards, and user rights (access/correction). For production, replace this summary with your approved legal text."
        ));
    }

    @GetMapping("/terms")
    public ResponseEntity<?> getTerms() {
        return ResponseEntity.ok(Map.of(
                "title", "Terms & Conditions (Summary)",
                "lastUpdated", "2026-03-21",
                "body", "This is a short summary for the in-app modal. Investments shown in the app are simulations unless integrated with a licensed payment provider. Farmers are responsible for accurate project information; investors should review risk scores and project details before investing. For production, replace this summary with your approved legal text."
        ));
    }
}
