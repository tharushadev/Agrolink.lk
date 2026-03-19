package com.agrolink.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "project_updates")
public class ProjectUpdate {

    public enum UpdateStatus {
        PENDING_VERIFICATION, // Farmer posted, waiting for admin
        VERIFIED              // Admin verified, visible to investors
    }

    @Id
    private String id;
    private String projectId;
    private String farmerId;
    private String content;
    private List<String> mediaUrls; // Photos/videos urls
    private UpdateStatus status = UpdateStatus.PENDING_VERIFICATION;
    private Date timestamp;

    public ProjectUpdate() {
        this.timestamp = new Date();
    }

    public ProjectUpdate(String projectId, String farmerId, String content, List<String> mediaUrls) {
        this.projectId = projectId;
        this.farmerId = farmerId;
        this.content = content;
        this.mediaUrls = mediaUrls;
        this.timestamp = new Date();
    }

    // --- GETTERS & SETTERS ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }

    public String getFarmerId() { return farmerId; }
    public void setFarmerId(String farmerId) { this.farmerId = farmerId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<String> getMediaUrls() { return mediaUrls; }
    public void setMediaUrls(List<String> mediaUrls) { this.mediaUrls = mediaUrls; }

    public UpdateStatus getStatus() { return status; }
    public void setStatus(UpdateStatus status) { this.status = status; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}