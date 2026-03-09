package com.agrolink.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.ArrayList;

@Document(collection = "farmer_projects")
public class FarmerProject {

    public enum ProjectStatus {
        PENDING_REVIEW, // Waiting for Gov Officer approval
        FUNDING,        // Approved and open for investment
        IN_PROGRESS,    // Minimum funding reached, farming started
        COMPLETED,      // Farming cycle done
        REJECTED        // Rejected by Gov Officer
    }

    @Id
    private String id;
    private String farmerId;

    // ✅ NEW SPECIFIC FIELDS
    private String projectTitle;
    private String cropType; // Front-end will lock this to "Paddy"
    private String location;
    private String description;

    private Double fundingGoal; // Expected total amount
    private Double minimumInvestment;
    private Integer durationInMonths;

    private Double expectedRevenue;
    private Double minRoi;
    private Double maxRoi;

    private List<String> photos = new ArrayList<>();

    // System Fields
    private ProjectStatus status = ProjectStatus.PENDING_REVIEW;
    private Double currentFundingAmount = 0.0;

    public FarmerProject() {}

    // --- GETTERS AND SETTERS ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFarmerId() { return farmerId; }
    public void setFarmerId(String farmerId) { this.farmerId = farmerId; }

    public String getProjectTitle() { return projectTitle; }
    public void setProjectTitle(String projectTitle) { this.projectTitle = projectTitle; }

    public String getCropType() { return cropType; }
    public void setCropType(String cropType) { this.cropType = cropType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getFundingGoal() { return fundingGoal; }
    public void setFundingGoal(Double fundingGoal) { this.fundingGoal = fundingGoal; }

    public Double getMinimumInvestment() { return minimumInvestment; }
    public void setMinimumInvestment(Double minimumInvestment) { this.minimumInvestment = minimumInvestment; }

    public Integer getDurationInMonths() { return durationInMonths; }
    public void setDurationInMonths(Integer durationInMonths) { this.durationInMonths = durationInMonths; }

    public Double getExpectedRevenue() { return expectedRevenue; }
    public void setExpectedRevenue(Double expectedRevenue) { this.expectedRevenue = expectedRevenue; }

    public Double getMinRoi() { return minRoi; }
    public void setMinRoi(Double minRoi) { this.minRoi = minRoi; }

    public Double getMaxRoi() { return maxRoi; }
    public void setMaxRoi(Double maxRoi) { this.maxRoi = maxRoi; }

    public List<String> getPhotos() { return photos; }
    public void setPhotos(List<String> photos) { this.photos = photos; }

    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }

    public Double getCurrentFundingAmount() { return currentFundingAmount; }
    public void setCurrentFundingAmount(Double currentFundingAmount) { this.currentFundingAmount = currentFundingAmount; }
}