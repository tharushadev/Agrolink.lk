package com.agrolink.service;

import com.agrolink.model.User;
import com.agrolink.model.DistrictRisk;
import com.agrolink.repository.RiskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RiskService {

    @Autowired
    private RiskRepository riskRepo;

    public DistrictRisk calculateHybridRisk(User user, String district, String season) {
        // 1. Get Base Risk from District Data (Using DistrictRisk repository)
        DistrictRisk baseData = riskRepo.findByDistrictAndSeason(district.toUpperCase(), season)
                .orElse(new DistrictRisk());

        if (baseData.baseRiskScore == null) {
            baseData.baseRiskScore = 50.0; // Fallback default
            baseData.riskLabel = "Unknown";
            return baseData;
        }

        // 2. Calculate Credibility using the integrated User module
        double credibility = calculateUserCredibility(user);

        // 3. Adjust the Risk
        // Logic: A high-credibility user can reduce project risk by up to 40%
        double reductionFactor = credibility * 0.40;
        double finalScore = baseData.baseRiskScore * (1 - reductionFactor);

        baseData.baseRiskScore = Math.round(finalScore * 10.0) / 10.0;
        baseData.riskLabel = getLabel(baseData.baseRiskScore);

        return baseData;
    }

    private double calculateUserCredibility(User u) {
        // Experience: weight 30% (maxes at 5 years)
        int exp = u.getYearsOfExperience() != null ? u.getYearsOfExperience() : 0;
        double expScore = Math.min(exp, 5) / 5.0;

        // Success Rate: weight 50%
        double successScore = 0.0;
        int total = u.getNumberOfProjects() != null ? u.getNumberOfProjects() : 0;
        int success = u.getSuccessfulProjects() != null ? u.getSuccessfulProjects() : 0;
        if (total > 0) {
            successScore = (double) success / total;
        }

        // Verification: weight 20%
        double verifyScore = (u.getIsVerified() != null && u.getIsVerified()) ? 1.0 : 0.0;

        return (expScore * 0.3) + (successScore * 0.5) + (verifyScore * 0.2);
    }

    private String getLabel(double score) {
        if (score < 25) return "Low Risk";
        if (score < 55) return "Medium Risk";
        return "High Risk";
    }
}