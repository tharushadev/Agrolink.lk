package com.agrolink.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app.demo-users")
public class DemoUsersProperties {

    /**
     * List of demo users to seed when app.seed-demo-users=true.
     * Configure via application.properties or environment variables.
     */
    private List<DemoUser> users = new ArrayList<>();

    public List<DemoUser> getUsers() {
        return users;
    }

    public void setUsers(List<DemoUser> users) {
        this.users = users;
    }

    public static class DemoUser {
        private String password;
        private String role;
        private String nic;
        private String phoneNumber;
        private String firstName;
        private String lastName;
        private String profileImageUrl;
        private String gnCertificateUrl;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getNic() {
            return nic;
        }

        public void setNic(String nic) {
            this.nic = nic;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
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

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }

        public String getGnCertificateUrl() {
            return gnCertificateUrl;
        }

        public void setGnCertificateUrl(String gnCertificateUrl) {
            this.gnCertificateUrl = gnCertificateUrl;
        }
    }
}
