package com.agrolink.controller;

import com.agrolink.model.FarmerProject;
import com.agrolink.model.User;
import com.agrolink.service.RiskService;
import com.agrolink.repository.FarmerProjectRepository;
import com.agrolink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiRiskController {

    @Autowired
    private FarmerProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RiskService riskService;

    @GetMapping("/risk/{projectId}")
    public ResponseEntity<?> calculateProjectRisk(@PathVariable String projectId) {

        // 1. Find the project
        Optional<FarmerProject> optProject = projectRepository.findById(projectId);
        if (!optProject.isPresent()) return ResponseEntity.badRequest().body("Project not found");
        FarmerProject project = optProject.get();

        // 2. Find the associated user (farmer)
        Optional<User> optUser = userRepository.findById(project.getFarmerId());
        if (!optUser.isPresent()) return ResponseEntity.badRequest().body("Farmer profile not found");
        User user = optUser.get();

        // 3. Determine District and Season
        // Using 'location' from FarmerProject as the district
        String district = project.getLocation() != null ? project.getLocation() : "Unknown";
        // Front-end or Project creation logic should provide season (defaulting to Maha)
        String season = "Maha";

        // 4. Calculate Risk
        var riskResult = riskService.calculateHybridRisk(user, district, season);

        // 5. Format Response
        Map<String, Object> response = new HashMap<>();
        response.put("riskScore", riskResult.baseRiskScore);
        response.put("riskLevel", riskResult.riskLabel);
        response.put("color", getRiskColor(riskResult.riskLabel));
        response.put("description", "AI Hybrid Analysis for " + district + " based on farmer credibility (" +
                user.getProfileStrength() + "% profile strength) and historical district yields.");

        return ResponseEntity.ok(response);
    }

    private String getRiskColor(String label) {
        if ("Low Risk".equalsIgnoreCase(label)) return "#76C442";
        if ("Medium Risk".equalsIgnoreCase(label)) return "#F5A623";
        return "#E05252";
    }
}