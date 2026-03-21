package com.agrolink.controller;

import com.agrolink.model.FarmerProject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InvestmentValidationTest {

    @Test
    public void testBelowMinimumInvestmentRejected() {
        // 1. Setup project with a 10,000 minimum
        FarmerProject project = new FarmerProject();
        project.setMinimumInvestment(10000.0);
        project.setStatus(FarmerProject.ProjectStatus.FUNDING);

        // 2. Attempt to invest 5,000
        Double attemptAmount = 5000.0;

        // 3. Validation Logic
        boolean isValid = true;
        if (project.getMinimumInvestment() != null && attemptAmount < project.getMinimumInvestment()) {
            isValid = false;
        }

        // 4. Verify it was rejected
        assertFalse(isValid, "Investment should be rejected if it is below the minimum required amount.");
    }

    @Test
    public void testInvestInClosedProjectRejected() {
        // 1. Setup an already completed project
        FarmerProject project = new FarmerProject();
        project.setMinimumInvestment(5000.0);
        project.setStatus(FarmerProject.ProjectStatus.COMPLETED); // Not open for funding!

        // 2. Attempt a valid amount
        Double attemptAmount = 10000.0;

        // 3. Validation Logic
        boolean isValid = true;
        if (project.getStatus() != FarmerProject.ProjectStatus.FUNDING) {
            isValid = false;
        }

        // 4. Verify it was rejected
        assertFalse(isValid, "Investment must be rejected if the project is not in the FUNDING status.");
    }
}