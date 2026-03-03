package com.example.demo.agrolink.controller;

import com.example.demo.agrolink.model.FarmerProject;
import com.example.demo.agrolink.repository.FarmerProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmer-project")
@CrossOrigin(origins = "*") // Allows mobile app to connect
public class FarmerProjectController {

    @Autowired
    private FarmerProjectRepository projectRepository;

    // 1. Create a new Project
    @PostMapping("/create")
    public FarmerProject createProject(@RequestBody FarmerProject project) {
        return projectRepository.save(project);
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
}
