package com.agrolink.controller;

import com.agrolink.model.FarmerProject;
import com.agrolink.repository.FarmerProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/farmer-project")
@CrossOrigin(origins = "*")
public class FarmerProjectController {

    @Autowired
    private FarmerProjectRepository projectRepository;

    // ─── FARMER ENDPOINTS ──────────────────────────────────────────────────

    // 1. Farmer creates a project (Locked to PENDING_REVIEW automatically)
    @PostMapping("/create")
    public ResponseEntity<FarmerProject> createProject(@RequestBody FarmerProject project) {
        project.setStatus(FarmerProject.ProjectStatus.PENDING_REVIEW);
        project.setCurrentFundingAmount(0.0);
        return ResponseEntity.ok(projectRepository.save(project));
    }

    // 2. Farmer views their own projects
    @GetMapping("/list/{farmerId}")
    public ResponseEntity<List<FarmerProject>> getFarmerProjects(@PathVariable String farmerId) {
        return ResponseEntity.ok(projectRepository.findByFarmerId(farmerId));
    }

    // ─── INVESTOR ENDPOINTS ────────────────────────────────────────────────

    // 3. Investors view ALL APPROVED/FUNDING projects
    @GetMapping("/active")
    public ResponseEntity<List<FarmerProject>> getActiveProjects() {
        return ResponseEntity.ok(projectRepository.findByStatus(FarmerProject.ProjectStatus.FUNDING));
    }

    // ─── GOV OFFICER (ADMIN) ENDPOINTS ─────────────────────────────────────

    // 4. Gov Officer views all pending projects
    @GetMapping("/admin/pending")
    public ResponseEntity<List<FarmerProject>> getPendingProjects() {
        return ResponseEntity.ok(projectRepository.findByStatus(FarmerProject.ProjectStatus.PENDING_REVIEW));
    }

    // 5. Gov Officer EDITS a project before approval
    @PutMapping("/admin/edit/{id}")
    public ResponseEntity<?> adminEditProject(@PathVariable String id, @RequestBody FarmerProject updatedData) {
        Optional<FarmerProject> optProject = projectRepository.findById(id);
        if (optProject.isPresent()) {
            FarmerProject project = optProject.get();
            // Gov Officer can adjust financial logic before approving
            project.setProjectTitle(updatedData.getProjectTitle());
            project.setFundingGoal(updatedData.getFundingGoal());
            project.setMinimumInvestment(updatedData.getMinimumInvestment());
            project.setMinRoi(updatedData.getMinRoi());
            project.setMaxRoi(updatedData.getMaxRoi());
            project.setDurationInMonths(updatedData.getDurationInMonths());

            return ResponseEntity.ok(projectRepository.save(project));
        }
        return ResponseEntity.badRequest().body("Project not found");
    }

    // 6. Gov Officer APPROVES a project (Pushes it to the system)
    @PostMapping("/admin/approve/{id}")
    public ResponseEntity<?> approveProject(@PathVariable String id) {
        Optional<FarmerProject> optProject = projectRepository.findById(id);
        if (optProject.isPresent()) {
            FarmerProject project = optProject.get();
            project.setStatus(FarmerProject.ProjectStatus.FUNDING); // Now visible to investors!
            return ResponseEntity.ok(projectRepository.save(project));
        }
        return ResponseEntity.badRequest().body("Project not found");
    }

    // 7. Gov Officer DELETES a project
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable String id) {
        projectRepository.deleteById(id);
        return ResponseEntity.ok("Project deleted by Gov Officer.");
    }

    // 8. Fetch ALL projects for the Investor Dashboard
    @GetMapping("/all")
    public ResponseEntity<?> getAllProjects() {
        try {
            // ✅ FIXED: Now using the lowercase 'projectRepository' instance variable
            List<FarmerProject> allProjects = projectRepository.findAll();
            return ResponseEntity.ok(allProjects);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching projects: " + e.getMessage());
        }
    }

    // 9. Fetch a single project by ID for the Project Details screen
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable String id) {
        Optional<FarmerProject> project = projectRepository.findById(id);

        if (project.isPresent()) {
            return ResponseEntity.ok(project.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}