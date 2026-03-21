package com.agrolink.controller;

import com.agrolink.model.FarmerProject;
import com.agrolink.model.Investment;
import com.agrolink.model.User;
import com.agrolink.repository.FarmerProjectRepository;
import com.agrolink.repository.InvestmentRepository;
import com.agrolink.repository.UserRepository;
import com.agrolink.service.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;

@RestController
@RequestMapping("/api/investments")
@CrossOrigin(origins = "*")
public class InvestmentController {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private FarmerProjectRepository projectRepository;

    // ✅ Added UserRepository to fetch names for the agreement
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AgreementService agreementService;

    // 1. Make an investment
    @PostMapping("/invest")
    public ResponseEntity<?> invest(@RequestBody Map<String, Object> requestData) {
        String projectId = requestData.get("projectId").toString();
        String investorId = requestData.get("investorId").toString();
        Double amount = Double.valueOf(requestData.get("amount").toString());

        Optional<FarmerProject> optProject = projectRepository.findById(projectId);
        if (!optProject.isPresent()) return ResponseEntity.badRequest().body("Project not found");

        FarmerProject project = optProject.get();

        if (project.getMinimumInvestment() != null && amount < project.getMinimumInvestment()) {
            return ResponseEntity.badRequest().body("Amount is less than the minimum investment.");
        }

        if (project.getStatus() != FarmerProject.ProjectStatus.FUNDING) {
            return ResponseEntity.badRequest().body("Project is not open for investment.");
        }

        Investment investment = new Investment(projectId, investorId, amount);
        investmentRepository.save(investment);

        // Fetch Names
        String investorName = "AgroLink Investor";
        Optional<User> optInvestor = userRepository.findById(investorId);
        if (optInvestor.isPresent()) investorName = optInvestor.get().getFirstName() + " " + optInvestor.get().getLastName();

        String farmerName = "AgroLink Farmer";
        Optional<User> optFarmer = userRepository.findById(project.getFarmerId());
        if (optFarmer.isPresent()) farmerName = optFarmer.get().getFirstName() + " " + optFarmer.get().getLastName();

        // 1. Generate Local PDF
        String pdfPath = agreementService.generateInvestmentAgreement(investorName, farmerName, project.getProjectTitle(), amount, investment.getId());

        // 2. Upload to Cloudinary
        try {

            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dhnl8fkno",
                    "api_key", "181573255974771",
                    "api_secret", "fDE61ZcH1doZGjNpAcz4NtixWY4"));

            File fileToUpload = new File(pdfPath);
            Map uploadResult = cloudinary.uploader().upload(fileToUpload, ObjectUtils.asMap("resource_type", "auto"));

            // 3. Save URL to Database
            String cloudUrl = uploadResult.get("secure_url").toString();
            investment.setAgreementUrl(cloudUrl);
            investmentRepository.save(investment);

            // Optional: Delete the local file after successful upload to save server space
            fileToUpload.delete();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cloudinary Upload Failed!");
        }

        // Update Project Funding
        Double newCurrent = project.getCurrentFundingAmount() + amount;
        project.setCurrentFundingAmount(newCurrent);
        if (newCurrent >= project.getFundingGoal()) project.setStatus(FarmerProject.ProjectStatus.IN_PROGRESS);
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