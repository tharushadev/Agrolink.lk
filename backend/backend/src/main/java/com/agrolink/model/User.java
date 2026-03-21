package com.agrolink.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String email;
    private String firstName;
    private String address;
    private List<String> skills = new ArrayList<>();
    private String lastName;
    private String phoneNumber;

    @JsonIgnore
    private String password;
    private String role;
    private String nic;
    private String profileImageUrl;

    private String gnCertificateUrl;

    private Double latitude;
    private Double longitude;
    private String city;

    private String profileImage;
    private Date createdAt = new Date(); // ✅ Automatically sets the date when registered
    private int profileStrength = 20;    // ✅ Changed default from 40 to 20

    // ✅ NEW: FARMER-SPECIFIC FIELDS
    // Using Integer and Boolean (capital letters) so they can be NULL for Investors
    private Integer yearsOfExperience;
    private Integer numberOfProjects;
    private Integer successfulProjects;
    private Boolean isVerified;

    public User() {}

    // --- STANDARD GETTERS & SETTERS ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getGnCertificateUrl() { return gnCertificateUrl; }
    public void setGnCertificateUrl(String gnCertificateUrl) { this.gnCertificateUrl = gnCertificateUrl; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public int getProfileStrength() { return profileStrength; }
    public void setProfileStrength(int profileStrength) { this.profileStrength = profileStrength; }

    // --- NEW GETTERS & SETTERS FOR FARMERS ---
    public Integer getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(Integer yearsOfExperience) { this.yearsOfExperience = yearsOfExperience; }

    public Integer getNumberOfProjects() { return numberOfProjects; }
    public void setNumberOfProjects(Integer numberOfProjects) { this.numberOfProjects = numberOfProjects; }

    public Integer getSuccessfulProjects() { return successfulProjects; }
    public void setSuccessfulProjects(Integer successfulProjects) { this.successfulProjects = successfulProjects; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    // Add these Getters & Setters at the bottom of the file
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
}