package com.agrolink.controller;

import com.agrolink.model.FarmerProject;
import com.agrolink.model.Investment;
import com.agrolink.repository.FarmerProjectRepository;
import com.agrolink.repository.InvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/farmer")
@CrossOrigin(origins = "*")
public class FarmerDashboardController {

    @Autowired
    private FarmerProjectRepository projectRepository;

    @Autowired
    private InvestmentRepository investmentRepository;

    @GetMapping("/dashboard/{farmerId}")
    public ResponseEntity<?> getFarmerDashboard(@PathVariable String farmerId) {
        List<FarmerProject> myProjects = projectRepository.findByFarmerId(farmerId);

        double totalRaised = myProjects.stream()
                .mapToDouble(p -> p.getCurrentFundingAmount() == null ? 0.0 : p.getCurrentFundingAmount())
                .sum();

        List<FarmerProject> activeProjects = myProjects.stream()
                .filter(p -> p.getStatus() == FarmerProject.ProjectStatus.FUNDING
                        || p.getStatus() == FarmerProject.ProjectStatus.IN_PROGRESS)
                .toList();

        Set<String> uniqueInvestorIds = new HashSet<>();
        for (FarmerProject p : myProjects) {
            if (p.getId() == null) continue;
            List<Investment> investments = investmentRepository.findByProjectId(p.getId());
            investments.stream()
                    .map(Investment::getInvestorId)
                    .filter(id -> id != null && !id.isBlank())
                    .forEach(uniqueInvestorIds::add);
        }

        // MVP: placeholder alerts (frontend can replace with real weather service later)
        List<Map<String, Object>> aiWeatherAlerts = List.of(
                Map.of("type", "weather", "severity", "medium", "message", "Possible rainfall in 48h"),
                Map.of("type", "pest", "severity", "low", "message", "Monitor for leaf blight signs")
        );

        return ResponseEntity.ok(Map.of(
                "totalRaisedLkr", totalRaised,
                "aiWeatherAlerts", aiWeatherAlerts,
                "activeProjects", activeProjects,
                "totalInvestors", uniqueInvestorIds.size()
        ));
    }

    @GetMapping("/analytics/{farmerId}")
    public ResponseEntity<?> getFarmerAnalytics(@PathVariable String farmerId) {
        List<FarmerProject> myProjects = projectRepository.findByFarmerId(farmerId);

        long pending = myProjects.stream().filter(p -> p.getStatus() == FarmerProject.ProjectStatus.PENDING_REVIEW).count();
        long funding = myProjects.stream().filter(p -> p.getStatus() == FarmerProject.ProjectStatus.FUNDING).count();
        long inProgress = myProjects.stream().filter(p -> p.getStatus() == FarmerProject.ProjectStatus.IN_PROGRESS).count();
        long completed = myProjects.stream().filter(p -> p.getStatus() == FarmerProject.ProjectStatus.COMPLETED).count();

        double totalGoal = myProjects.stream().mapToDouble(p -> p.getFundingGoal() == null ? 0.0 : p.getFundingGoal()).sum();
        double totalRaised = myProjects.stream().mapToDouble(p -> p.getCurrentFundingAmount() == null ? 0.0 : p.getCurrentFundingAmount()).sum();

        return ResponseEntity.ok(Map.of(
                "projectCount", myProjects.size(),
                "statusCounts", Map.of(
                        "pending", pending,
                        "funding", funding,
                        "inProgress", inProgress,
                        "completed", completed
                ),
                "totalFundingGoalLkr", totalGoal,
                "totalRaisedLkr", totalRaised
        ));
    }
}
