package com.agrolink.controller;

import com.agrolink.model.Investment;
import com.agrolink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/kpis")
    public ResponseEntity<?> getPlatformKPIs() {

        // 1. Calculate Real Investors
        long investorCount = userRepository.findAll().stream()
                .filter(u -> "INVESTOR".equalsIgnoreCase(u.getRole()))
                .count();

        // 2. Active Crops (Hardcoded to 1 for Paddy per your requirement)
        long activeCrops = 1;

        // 3. Calculate "Funded Today"
        Date startOfDay = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Investment> allInvestments = mongoTemplate.findAll(Investment.class);

        double fundedToday = allInvestments.stream()
                .filter(inv -> inv.getTimestamp().after(startOfDay))
                .mapToDouble(Investment::getAmount)
                .sum();

        // 4. Format the response
        return ResponseEntity.ok(Map.of(
                "activeCrops", String.valueOf(activeCrops),
                "investors", formatNumber(investorCount),
                "fundedToday", "$" + formatNumber((long)fundedToday),
                "avgReturn", "18%"
        ));
    }

    private String formatNumber(long number) {
        if (number >= 1000) {
            return String.format("%.1fk", number / 1000.0);
        }
        return String.valueOf(number);
    }
}