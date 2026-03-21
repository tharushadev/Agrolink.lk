package com.agrolink.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profile/support")
@CrossOrigin(origins = "*")
public class SupportController {

    @GetMapping
    public ResponseEntity<?> getSupportInfo() {
        return ResponseEntity.ok(Map.of(
                "phone", "+94-11-000-0000",
                "email", "support@agrolink.lk",
                "liveChat", Map.of(
                        "enabled", true,
                        "path", "/api/chat"
                )
        ));
    }
}
