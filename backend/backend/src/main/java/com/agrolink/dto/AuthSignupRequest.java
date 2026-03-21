package com.agrolink.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthSignupRequest {
    @NotBlank
    private String role; // FARMER or INVESTOR

    @NotBlank
    private String password;

    private String firstName;
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @JsonAlias({"phone", "mobile", "mobileNumber"})
    private String phoneNumber;

        @NotBlank(message = "NIC is required")
        @Pattern(
            regexp = "^(\\d{12}|\\d{9}[Vv])$",
            message = "NIC must be either 12 digits, or 9 digits followed by V"
        )
        private String nic;

    // For Farmer onboarding uploads, frontend can send uploaded URLs.
    private String farmerPhotoUrl;
    private String gnCertificateUrl;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getFarmerPhotoUrl() {
        return farmerPhotoUrl;
    }

    public void setFarmerPhotoUrl(String farmerPhotoUrl) {
        this.farmerPhotoUrl = farmerPhotoUrl;
    }

    public String getGnCertificateUrl() {
        return gnCertificateUrl;
    }

    public void setGnCertificateUrl(String gnCertificateUrl) {
        this.gnCertificateUrl = gnCertificateUrl;
    }
}
