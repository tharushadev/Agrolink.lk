package com.example.demo.ai;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "farmers")
public class Farmer {
    @Id
    public String id;
    public String name;
    public int yearsExperience;    // How long they've been farming
    public int totalProjects;      // Total projects on the app
    public int successfulProjects; // How many were profitable
    public boolean isVerified;     // Did the Govt Officer verify them?

    // Empty Constructor (Needed for MongoDB)
    public Farmer() {}

    // Easy Constructor (For testing)
    public Farmer(String id, String name, int years, int total, int success, boolean verified) {
        this.id = id;
        this.name = name;
        this.yearsExperience = years;
        this.totalProjects = total;
        this.successfulProjects = success;
        this.isVerified = verified;
    }
}