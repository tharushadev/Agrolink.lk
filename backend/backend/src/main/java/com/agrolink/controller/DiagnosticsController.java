package com.agrolink.controller;

import com.agrolink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/diagnostics")
@CrossOrigin(origins = "*")
public class DiagnosticsController {

    private final UserRepository userRepository;

    @Value("${spring.data.mongodb.uri:}")
    private String mongoUri;

    public DiagnosticsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/mongo")
    public ResponseEntity<?> mongo() {
        long usersCount;
        try {
            usersCount = userRepository.count();
        } catch (Exception ex) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("message", "Unable to query MongoDB");
            body.put("error", ex.getClass().getSimpleName());
            body.put("details", safeCauseChain(ex, 6));
            body.put("mongoUri", sanitizeMongoUri(mongoUri));
            body.put("database", extractDatabaseName(mongoUri));
            body.put("hints", new String[]{
                    "If using Atlas: ensure Network Access allows your IP (or 0.0.0.0/0 temporarily)",
                    "Ensure the connection string includes a database name (e.g., ...mongodb.net/agrolink?...) ",
                    "Ensure the DB user/password is correct and has readWrite on the DB",
                    "If you changed Atlas password, update MONGODB_URI and restart the backend"
            });
            return ResponseEntity.status(500).body(body);
        }

        return ResponseEntity.ok(Map.of(
                "mongoUri", sanitizeMongoUri(mongoUri),
                "database", extractDatabaseName(mongoUri),
                "usersCollection", "users",
                "usersCount", usersCount
        ));
    }

    private String extractDatabaseName(String uri) {
        if (uri == null || uri.isBlank()) {
            return "";
        }
        int schemeIdx = uri.indexOf("://");
        int start = schemeIdx >= 0 ? schemeIdx + 3 : 0;

        // Skip credentials if present
        int atIdx = uri.indexOf('@', start);
        int hostStart = atIdx >= 0 ? atIdx + 1 : start;

        // Find first '/' after host list
        int slashIdx = uri.indexOf('/', hostStart);
        if (slashIdx < 0) {
            return "";
        }

        int endIdx = uri.indexOf('?', slashIdx + 1);
        if (endIdx < 0) {
            endIdx = uri.length();
        }

        String db = uri.substring(slashIdx + 1, endIdx).trim();
        return db;
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

        return uri.substring(0, credsStart) + "***:***@" + uri.substring(atIdx + 1);
    }

    private String[] safeCauseChain(Throwable ex, int maxDepth) {
        if (ex == null || maxDepth <= 0) {
            return new String[0];
        }

        java.util.ArrayList<String> items = new java.util.ArrayList<>();
        Throwable cur = ex;
        int depth = 0;
        while (cur != null && depth < maxDepth) {
            String type = cur.getClass().getSimpleName();
            String msg = cur.getMessage();
            if (msg == null) {
                msg = "";
            }
            // Avoid accidentally echoing full URIs if any library includes them.
            msg = msg.replaceAll("mongodb\\+srv://[^\\s]+", "mongodb+srv://***");
            msg = msg.replaceAll("mongodb://[^\\s]+", "mongodb://***");
            items.add(type + ": " + msg);
            cur = cur.getCause();
            depth++;
        }
        return items.toArray(new String[0]);
    }
}
