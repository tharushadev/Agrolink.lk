package com.agrolink.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "investments")
public class Investment {

    @Id
    private String id;
    private String projectId;
    private String investorId;
    private Double amount;
    private Date timestamp;

    public Investment() {
        this.timestamp = new Date();
    }

    public Investment(String projectId, String investorId, Double amount) {
        this.projectId = projectId;
        this.investorId = investorId;
        this.amount = amount;
        this.timestamp = new Date();
    }

    // --- GETTERS & SETTERS ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }

    public String getInvestorId() { return investorId; }
    public void setInvestorId(String investorId) { this.investorId = investorId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}