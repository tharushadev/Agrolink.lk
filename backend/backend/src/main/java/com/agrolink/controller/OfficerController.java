package com.agrolink.controller;

import com.agrolink.model.FarmerProject;
import com.agrolink.model.User;
import com.agrolink.repository.FarmerProjectRepository;
import com.agrolink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/officer")
@CrossOrigin(origins = "*")
public class OfficerController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FarmerProjectRepository projectRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 0. Register Officer
    @PostMapping("/register")
    public ResponseEntity<?> registerOfficer(@RequestBody User officer) {
        officer.setPassword(passwordEncoder.encode(officer.getPassword()));
        officer.setRole("officer");
        officer.setProfileStrength(100);
        userRepository.save(officer);
        return ResponseEntity.ok(Map.of("message", "Officer registered successfully with hashed password!"));
    }

    // 1. ✅ FIXED: Get ALL farmers for the Global Officer MVP
    @GetMapping("/farmers")
    public ResponseEntity<List<User>> getAllFarmers() {
        // This will find "FARMER", "farmer", or "Farmer" anywhere in the country
        List<User> farmers = userRepository.findByRoleIgnoreCase("FARMER");
        return ResponseEntity.ok(farmers);
    }

    // 2. Toggle Farmer Verification (Verify/Unverify)
    @PutMapping("/farmer/{farmerId}/toggle-verify")
    public ResponseEntity<?> toggleFarmerVerification(@PathVariable String farmerId) {
        Optional<User> optFarmer = userRepository.findById(farmerId);
        if (!optFarmer.isPresent()) return ResponseEntity.badRequest().body("Farmer not found");

        User farmer = optFarmer.get();
        boolean currentStatus = farmer.getIsVerified() != null && farmer.getIsVerified();
        farmer.setIsVerified(!currentStatus);

        userRepository.save(farmer);
        return ResponseEntity.ok(Map.of("message", "Status updated", "isVerified", farmer.getIsVerified()));
    }

    // 3. Get all projects for a specific farmer
    @GetMapping("/farmer/{farmerId}/projects")
    public ResponseEntity<List<FarmerProject>> getFarmerProjects(@PathVariable String farmerId) {
        List<FarmerProject> projects = projectRepository.findByFarmerId(farmerId);
        return ResponseEntity.ok(projects);
    }

    // 4. Change Project Status
    @PutMapping("/project/{projectId}/status")
    public ResponseEntity<?> updateProjectStatus(@PathVariable String projectId, @RequestBody Map<String, String> request) {
        Optional<FarmerProject> optProject = projectRepository.findById(projectId);
        if (!optProject.isPresent()) return ResponseEntity.badRequest().body("Project not found");

        try {
            FarmerProject.ProjectStatus newStatus = FarmerProject.ProjectStatus.valueOf(request.get("status"));
            FarmerProject project = optProject.get();
            project.setStatus(newStatus);
            projectRepository.save(project);
            return ResponseEntity.ok(project);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid project status");
        }
    }

    // 5. Delete a Project
    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId) {
        if (!projectRepository.existsById(projectId)) {
            return ResponseEntity.badRequest().body("Project not found");
        }
        projectRepository.deleteById(projectId);
        return ResponseEntity.ok(Map.of("message", "Project deleted successfully"));
    }
}