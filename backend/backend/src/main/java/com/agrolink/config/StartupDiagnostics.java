package com.agrolink.config;

import com.agrolink.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupDiagnostics {

    private static final Logger log = LoggerFactory.getLogger(StartupDiagnostics.class);

    private final UserRepository userRepository;

    @Value("${spring.data.mongodb.uri:}")
    private String mongoUri;

    public StartupDiagnostics(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        long userCount;
        try {
            userCount = userRepository.count();
        } catch (Exception ex) {
            log.warn("Startup diagnostics: unable to count users", ex);
            return;
        }

        log.info("Startup diagnostics: mongoUri={} usersCollectionCount={}", sanitizeMongoUri(mongoUri), userCount);
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
}
