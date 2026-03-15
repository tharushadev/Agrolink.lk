package com.example.demo.ai;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "district_risks")
public class DistrictRisk {
    @Id
    public String id;
    public String district;
    public String season;
    public Double baseRiskScore;
    public String riskLabel;

    // If not using Lombok, generate Getters/Setters here!
}