package com.agrolink.controller;

import com.agrolink.model.FarmerProject;
import com.agrolink.model.Investment;
import com.agrolink.repository.FarmerProjectRepository;
import com.agrolink.repository.InvestmentRepository;
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
        if (project.getMinimumInvestment() != null && amount < project.getMinimumInvestment()) {
            return ResponseEntity.badRequest().body("Amount is less than the minimum investment: " + project.getMinimumInvestment());
        }

        // Check if project is taking funds (Must be approved by Gov Officer)
        if (project.getStatus() != FarmerProject.ProjectStatus.FUNDING) {
            return ResponseEntity.badRequest().body("Project is not currently open for investment");
        }

        // Save Investment
        Investment investment = new Investment(projectId, investorId, amount);
        investmentRepository.save(investment);

        // Update Project Funding
        Double newCurrent = project.getCurrentFundingAmount() + amount;
        project.setCurrentFundingAmount(newCurrent);

        // Auto-change status to IN_PROGRESS if funding goal is reached
        if (newCurrent >= project.getFundingGoal()) {
            project.setStatus(FarmerProject.ProjectStatus.IN_PROGRESS);
        }

        projectRepository.save(project);

        return ResponseEntity.ok(Map.of(
                "message", "Investment successful!",
                "investment", investment,
                "projectStatus", project.getStatus()
        ));
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