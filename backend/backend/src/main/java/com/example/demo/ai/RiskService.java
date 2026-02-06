package com.example.demo.ai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class RiskService {

    @Autowired
    private RiskRepository riskRepo; // Reads District Data

    @Autowired
    private FarmerRepository farmerRepo; // Reads Farmer Data

    public DistrictRisk calculateHybridRisk(String farmerId, String district, String season) {
        // 1. Get Base Risk (From your JSON import)
        DistrictRisk baseData = riskRepo.findByDistrictAndSeason(district.toUpperCase(), season)
                .orElse(new DistrictRisk()); // Default empty if not found

        // If district not found, return empty or handle error
        if (baseData.baseRiskScore == null) {
            baseData.baseRiskScore = 50.0; // Default fallback
            baseData.riskLabel = "Unknown";
            return baseData;
        }

        // 2. Get Farmer Profile
        Optional<Farmer> farmerOpt = farmerRepo.findById(farmerId);

        // If farmer doesn't exist (or new user), use Base Risk only
        if (farmerOpt.isEmpty()) {
            return baseData;
        }

        Farmer farmer = farmerOpt.get();

        // 3. Calculate Credibility Score (0.0 to 1.0)
        double credibility = calculateCredibility(farmer);

        // 4. Adjust the Risk
        // Logic: A perfect farmer can reduce risk by max 40% (0.40)
        double reductionFactor = credibility * 0.40;
        double finalScore = baseData.baseRiskScore * (1 - reductionFactor);

        // 5. Update the Result Object
        baseData.baseRiskScore = Math.round(finalScore * 10.0) / 10.0; // Round to 1 decimal
        baseData.riskLabel = getLabel(baseData.baseRiskScore);

        return baseData;
    }

    private double calculateCredibility(Farmer f) {
        // Weight: Experience (30%), Success Rate (50%), Verified (20%)

        double expScore = Math.min(f.yearsExperience, 5) / 5.0; // Max out at 5 years

        double successScore = 0.0;
        if (f.totalProjects > 0) {
            successScore = (double) f.successfulProjects / f.totalProjects;
        }

        double verifyScore = f.isVerified ? 1.0 : 0.0;

        return (expScore * 0.3) + (successScore * 0.5) + (verifyScore * 0.2);
    }

    private String getLabel(double score) {
        if (score < 25) return "Low Risk";
        if (score < 55) return "Medium Risk";
        return "High Risk";
    }
}