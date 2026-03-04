package com.example.demo.agrolink.service;

import com.example.demo.agrolink.model.FarmerProject;
import com.example.demo.agrolink.repository.FarmerProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FarmerProjectService {

    @Autowired
    private FarmerProjectRepository projectRepository;

    // 1. Farmer creates a new project (Pending by default)
    public FarmerProject createProject(FarmerProject project) {
        project.setStatus(FarmerProject.ProjectStatus.PENDING_REVIEW);
        return projectRepository.save(project);
    }

    // 2. Admin gets all pending projects
    public List<FarmerProject> getPendingProjects() {
        return projectRepository.findByStatus(FarmerProject.ProjectStatus.PENDING_REVIEW);
    }

    // 3. Admin approves a project with financial details
    public FarmerProject approveProject(String projectId, Double goal, Double minStart, Double minInvest, String yield,
            String income) {
        Optional<FarmerProject> optProject = projectRepository.findById(projectId);
        if (optProject.isPresent()) {
            FarmerProject project = optProject.get();
            project.setTotalFundingGoal(goal);
            project.setMinimumFundingToStart(minStart);
            project.setMinimumInvestmentPerInvestor(minInvest);
            project.setVerifiedExpectedYield(yield);
            project.setEstimatedIncome(income);
            project.setStatus(FarmerProject.ProjectStatus.APPROVED);
            return projectRepository.save(project);
        }
        throw new RuntimeException("Project not found");
    }

    // 4. Get all approved projects (For Investors to see)
    public List<FarmerProject> getApprovedProjects() {
        return projectRepository.findByStatus(FarmerProject.ProjectStatus.APPROVED);
    }
}
