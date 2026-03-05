package com.example.demo.agrolink.controller;

import com.example.demo.agrolink.model.FarmerProject;
import com.example.demo.agrolink.repository.FarmerProjectRepository;
import com.example.demo.agrolink.service.FarmerProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmer-project")
@CrossOrigin(origins = "*") // Allows mobile app to connect
public class FarmerProjectController {

    @Autowired
    private FarmerProjectRepository projectRepository;

    @Autowired
    private FarmerProjectService projectService;

    // 1. Create a new Project (now routes through Service to set PENDING status)
    @PostMapping("/create")
    public FarmerProject createProject(@RequestBody FarmerProject project) {
        return projectService.createProject(project);
    }

    // 2. Get all projects for a specific farmer
    @GetMapping("/list/{farmerId}")
    public List<FarmerProject> getFarmerProjects(@PathVariable String farmerId) {
        return projectRepository.findByFarmerId(farmerId);
    }

    // 3. Get ALL projects (for debugging)
    @GetMapping("/all")
    public List<FarmerProject> getAllProjects() {
        return projectRepository.findAll();
    }

    // 4. Delete a Project
    @DeleteMapping("/delete/{id}")
    public String deleteProject(@PathVariable String id) {
        projectRepository.deleteById(id);
        return "Project deleted successfully!";
    }

    // 5. Update a Project
    @PutMapping("/update/{id}")
    public FarmerProject updateProject(@PathVariable String id, @RequestBody FarmerProject updatedData) {
        return projectRepository.findById(id)
                .map(project -> {
                    project.setProjectName(updatedData.getProjectName());
                    project.setLocation(updatedData.getLocation());
                    project.setCropType(updatedData.getCropType());
                    project.setExpectedYield(updatedData.getExpectedYield());
                    project.setStartDate(updatedData.getStartDate());
                    return projectRepository.save(project);
                })
                .orElse(null); // Return null if not found
    }

    // --- ADMIN & INVESTOR ENDPOINTS ---

    // 6. Get all pending projects for Admin Review
    @GetMapping("/pending")
    public List<FarmerProject> getPendingProjects() {
        return projectService.getPendingProjects();
    }

    // 7. Admin approves a project and sets financial goals
    @PostMapping("/{projectId}/approve")
    public org.springframework.http.ResponseEntity<?> approveProject(@PathVariable String projectId,
            @RequestBody java.util.Map<String, Object> approvalData) {
        try {
            Double goal = Double.valueOf(approvalData.get("totalFundingGoal").toString());
            Double minStart = Double.valueOf(approvalData.get("minimumFundingToStart").toString());
            Double minInvest = Double.valueOf(approvalData.get("minimumInvestmentPerInvestor").toString());
            String yield = approvalData.get("verifiedExpectedYield").toString();
            String income = approvalData.get("estimatedIncome").toString();

            Double unitPrice = null;
            Integer totalUnits = null;

            if (approvalData.get("unitPrice") != null) {
                unitPrice = Double.valueOf(approvalData.get("unitPrice").toString());
            }

            if (approvalData.get("totalUnits") != null) {
                totalUnits = Integer.valueOf(approvalData.get("totalUnits").toString());
            }

            FarmerProject approvedProject = projectService.approveProject(projectId, goal, minStart, minInvest, yield,
                    income, unitPrice, totalUnits);
            return org.springframework.http.ResponseEntity.ok(approvedProject);
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest()
                    .body("Error approving project: " + e.getMessage());
        }
    }

    // 8. Get all approved projects ready for funding (For Investors)
    @GetMapping("/approved")
    public List<FarmerProject> getApprovedProjects() {
        return projectService.getApprovedProjects();
    }
}
