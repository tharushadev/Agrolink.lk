package com.example.demo.agrolink.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "farmer_projects")
public class FarmerProject {
    @Id
    private String id;
    private String farmerId; // Links this project to a specific Farmer/User
    private String projectName; // e.g., "Maha Season Rice"
    private String location; // e.g., "Kandy"
    private String cropType; // e.g., "Rice", "Corn"
    private String expectedYield; // e.g., "1000kg"
    private String startDate; // e.g., "2026-02-15"

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
}
