package com.example.demo.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk")
@CrossOrigin(origins = "*")
public class RiskController {

    @Autowired
    private RiskService riskService;

    // Example URL: /api/risk/predict?farmerId=123&district=KANDY&season=Yala
    @GetMapping("/predict")
    public DistrictRisk predict(
            @RequestParam String farmerId,
            @RequestParam String district,
            @RequestParam String season) {

        return riskService.calculateHybridRisk(farmerId, district, season);
    }
}