package com.example.demo.agrolink.controller;

import com.example.demo.agrolink.dto.PurchaseUnitsRequest;
import com.example.demo.agrolink.model.FarmerProject;
import com.example.demo.agrolink.model.Investment;
import com.example.demo.agrolink.repository.FarmerProjectRepository;
import com.example.demo.agrolink.repository.InvestmentRepository;
import com.example.demo.agrolink.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/investments")
@CrossOrigin(origins = "*")
public class InvestmentController {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private FarmerProjectRepository projectRepository;

    @Autowired
    private InvestmentService investmentService;

    // 1. Make an investment
    @PostMapping("/invest")
    public ResponseEntity<?> invest(@RequestBody Map<String, Object> requestData) {
        String projectId = requestData.get("projectId").toString();
        String investorId = requestData.get("investorId").toString();
        Double amount = Double.valueOf(requestData.get("amount").toString());

        Optional<FarmerProject> optProject = projectRepository.findById(projectId);
        if (!optProject.isPresent()) {
            return ResponseEntity.badRequest().body("Project not found");
        }

        FarmerProject project = optProject.get();

        // Check Minimum Investment amount
        if (project.getMinimumInvestmentPerInvestor() != null && amount < project.getMinimumInvestmentPerInvestor()) {
            return ResponseEntity.badRequest()
                    .body("Amount is less than the minimum investment: " + project.getMinimumInvestmentPerInvestor());
        }

        // Check if project is taking funds
        if (project.getStatus() != FarmerProject.ProjectStatus.APPROVED &&
                project.getStatus() != FarmerProject.ProjectStatus.FUNDING) {
            return ResponseEntity.badRequest().body("Project is not open for investment");
        }

        // Save Investment
        Investment investment = Investment.builder()
            .projectId(projectId)
            .investorId(investorId)
            .amount(amount)
            .build();
        investmentRepository.save(investment);

        // Update Project
        Double newCurrent = project.getCurrentFundingAmount() + amount;
        project.setCurrentFundingAmount(newCurrent);

        // Change status to IN_PROGRESS if minimum required to start is met
        if (project.getMinimumFundingToStart() != null && newCurrent >= project.getMinimumFundingToStart()) {
            project.setStatus(FarmerProject.ProjectStatus.IN_PROGRESS);
        } else if (project.getStatus() == FarmerProject.ProjectStatus.APPROVED) {
            project.setStatus(FarmerProject.ProjectStatus.FUNDING); // It has some funds now
        }

        projectRepository.save(project);

        return ResponseEntity.ok(Map.of("message", "Investment successful!", "investment", investment, "projectStatus",
                project.getStatus()));
    }

    @PostMapping("/purchase-units")
    public ResponseEntity<?> purchaseUnits(@RequestBody PurchaseUnitsRequest requestData) {
        if (requestData.getProjectId() == null || requestData.getInvestorId() == null || requestData.getQuantity() == null) {
            return ResponseEntity.badRequest().body("projectId, investorId and quantity are required");
        }

        try {
            Investment investment = investmentService.purchaseUnits(
                    requestData.getProjectId(),
                    requestData.getInvestorId(),
                    requestData.getQuantity());

            return ResponseEntity.ok(Map.of(
                    "message", "Unit purchase successful!",
                    "investment", investment,
                    "availableUnits", investment.getAvailableUnits()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Get investments for a project
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Investment>> getProjectInvestments(@PathVariable String projectId) {
        return ResponseEntity.ok(investmentRepository.findByProjectId(projectId));
    }

    // 3. Get investments for an investor
    @GetMapping("/investor/{investorId}")
    public ResponseEntity<List<Investment>> getInvestorInvestments(@PathVariable String investorId) {
        return ResponseEntity.ok(investmentRepository.findByInvestorId(investorId));
    }
}
