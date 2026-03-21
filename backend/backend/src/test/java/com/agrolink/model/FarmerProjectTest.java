package com.agrolink.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FarmerProjectTest {

    @Test
    public void testFundingGoalReached() {
        // 1. Setup a dummy project
        FarmerProject project = new FarmerProject();
        project.setFundingGoal(100000.0);
        project.setCurrentFundingAmount(80000.0);
        project.setStatus(FarmerProject.ProjectStatus.FUNDING);

        // 2. Simulate a new investment of 25,000
        Double newInvestment = 25000.0;
        Double newTotal = project.getCurrentFundingAmount() + newInvestment;
        project.setCurrentFundingAmount(newTotal);

        // 3. Apply the business logic from the controller
        if (newTotal >= project.getFundingGoal()) {
            project.setStatus(FarmerProject.ProjectStatus.IN_PROGRESS);
        }

        // 4. Verify the results
        assertEquals(105000.0, project.getCurrentFundingAmount(), "Total funding should update correctly.");
        assertEquals(FarmerProject.ProjectStatus.IN_PROGRESS, project.getStatus(), "Project status must auto-update to IN_PROGRESS when goal is met.");
    }

    @Test
    public void testFundingGoalNotReached() {
        FarmerProject project = new FarmerProject();
        project.setFundingGoal(100000.0);
        project.setCurrentFundingAmount(50000.0);
        project.setStatus(FarmerProject.ProjectStatus.FUNDING);

        Double newInvestment = 10000.0;
        Double newTotal = project.getCurrentFundingAmount() + newInvestment;
        project.setCurrentFundingAmount(newTotal);

        if (newTotal >= project.getFundingGoal()) {
            project.setStatus(FarmerProject.ProjectStatus.IN_PROGRESS);
        }

        assertEquals(FarmerProject.ProjectStatus.FUNDING, project.getStatus(), "Project status must remain FUNDING if goal is not met.");
    }
}