package com.agrolink.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;

    // ✅ New Fields from your UI update
    private String firstName;
    private String lastName;

    // ✅ Phone number is now the ONLY way to log in
    private String phoneNumber;
    private String password;

    private String role;
    private String nic;

    // ✅ Added field for the Grama Niladari Certificate (Will store the image URL/Path)
    private String gnCertificateUrl;

    private String profileImage;
    private int profileStrength = 40;

    public User() {}

    public User(String firstName, String lastName, String phoneNumber, String password, String role, String nic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.nic = nic;
    }

    // --- GETTERS & SETTERS ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

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

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public int getProfileStrength() { return profileStrength; }
    public void setProfileStrength(int profileStrength) { this.profileStrength = profileStrength; }
}