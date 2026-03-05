package com.example.demo.agrolink.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.ArrayList;

@Document(collection = "farmer_projects")
public class FarmerProject {

    public enum ProjectStatus {
        PENDING_REVIEW, // Waiting for Admin approval
        APPROVED, // Approved by Admin, waiting for funding to open
        FUNDING, // Open for investment
        IN_PROGRESS, // Minimum funding reached, farming matched
        COMPLETED, // Farming cycle done
        REJECTED // Rejected by admin
    }

    @Id
    private String id;
    private String farmerId; // Links this project to a specific Farmer/User
    private String projectName; // e.g., "Maha Season Rice"
    private String location; // e.g., "Kandy"
    private String cropType; // e.g., "Rice", "Corn"
    private String expectedYield; // e.g., "1000kg"
    private String startDate; // e.g., "2026-02-15"

    // New Fields
    private ProjectStatus status = ProjectStatus.PENDING_REVIEW;
    private List<String> mediaUrls = new ArrayList<>(); // Links to photos/docs

    // Admin Fields
    private Double totalFundingGoal; // Set by admin
    private Double minimumFundingToStart; // Set by admin
    private Double currentFundingAmount = 0.0;
    private Double unitPrice;
    private Integer totalUnits;
    private Integer availableUnits;
    private Double minimumInvestmentPerInvestor; // Set by admin
    private String verifiedExpectedYield; // Set by admin
    private String estimatedIncome; // Set by admin

    // Constructors
    public FarmerProject() {
    }

    public FarmerProject(String farmerId, String projectName, String location, String cropType, String expectedYield,
            String startDate) {
        this.farmerId = farmerId;
        this.projectName = projectName;
        this.location = location;
        this.cropType = cropType;
        this.expectedYield = expectedYield;
        this.startDate = startDate;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public String getExpectedYield() {
        return expectedYield;
    }

    public void setExpectedYield(String expectedYield) {
        this.expectedYield = expectedYield;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }

    public Double getTotalFundingGoal() {
        return totalFundingGoal;
    }

    public void setTotalFundingGoal(Double totalFundingGoal) {
        this.totalFundingGoal = totalFundingGoal;
    }

    public Double getMinimumFundingToStart() {
        return minimumFundingToStart;
    }

    public void setMinimumFundingToStart(Double minimumFundingToStart) {
        this.minimumFundingToStart = minimumFundingToStart;
    }

    public Double getCurrentFundingAmount() {
        return currentFundingAmount;
    }

    public void setCurrentFundingAmount(Double currentFundingAmount) {
        this.currentFundingAmount = currentFundingAmount;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(Integer totalUnits) {
        this.totalUnits = totalUnits;
    }

    public Integer getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(Integer availableUnits) {
        this.availableUnits = availableUnits;
    }

    public Double getMinimumInvestmentPerInvestor() {
        return minimumInvestmentPerInvestor;
    }

    public void setMinimumInvestmentPerInvestor(Double minimumInvestmentPerInvestor) {
        this.minimumInvestmentPerInvestor = minimumInvestmentPerInvestor;
    }

    public String getVerifiedExpectedYield() {
        return verifiedExpectedYield;
    }

    public void setVerifiedExpectedYield(String verifiedExpectedYield) {
        this.verifiedExpectedYield = verifiedExpectedYield;
    }

    public String getEstimatedIncome() {
        return estimatedIncome;
    }

    public void setEstimatedIncome(String estimatedIncome) {
        this.estimatedIncome = estimatedIncome;
    }
}
