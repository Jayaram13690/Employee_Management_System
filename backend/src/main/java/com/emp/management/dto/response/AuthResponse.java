package com.emp.management.dto.response;

import com.emp.management.enums.Role;

/**
 * DTO for authentication responses containing JWT token and user info.
 */
public class AuthResponse {

    private String token;
    private String refreshToken;
    private String email;
    private Role role;
    private Long userId;
    private Long employeeId;
    private String fullName;
    private String phone;
    private String profilePhoto;
    private String bio;
    private String educationDegree;
    private String educationInstitution;
    private String educationBranch;
    private String educationPassingYear;
    private String skills;
    private String address;

    // ===== Constructors =====

    public AuthResponse() {
    }

    public AuthResponse(String token, String refreshToken, String email, Role role, Long userId, Long employeeId, String fullName, String phone, String profilePhoto, String bio, String educationDegree, String educationInstitution, String educationBranch, String educationPassingYear, String skills, String address) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.email = email;
        this.role = role;
        this.userId = userId;
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.phone = phone;
        this.profilePhoto = profilePhoto;
        this.bio = bio;
        this.educationDegree = educationDegree;
        this.educationInstitution = educationInstitution;
        this.educationBranch = educationBranch;
        this.educationPassingYear = educationPassingYear;
        this.skills = skills;
        this.address = address;
    }

    // ===== Getters and Setters =====

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEducationDegree() {
        return educationDegree;
    }

    public void setEducationDegree(String educationDegree) {
        this.educationDegree = educationDegree;
    }

    public String getEducationInstitution() {
        return educationInstitution;
    }

    public void setEducationInstitution(String educationInstitution) {
        this.educationInstitution = educationInstitution;
    }

    public String getEducationBranch() {
        return educationBranch;
    }

    public void setEducationBranch(String educationBranch) {
        this.educationBranch = educationBranch;
    }

    public String getEducationPassingYear() {
        return educationPassingYear;
    }

    public void setEducationPassingYear(String educationPassingYear) {
        this.educationPassingYear = educationPassingYear;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // ===== Builder Pattern Support =====

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String token;
        private String refreshToken;
        private String email;
        private Role role;
        private Long userId;
        private Long employeeId;
        private String fullName;
        private String phone;
        private String profilePhoto;
        private String bio;
        private String educationDegree;
        private String educationInstitution;
        private String educationBranch;
        private String educationPassingYear;
        private String skills;
        private String address;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder employeeId(Long employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder profilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder educationDegree(String educationDegree) {
            this.educationDegree = educationDegree;
            return this;
        }

        public Builder educationInstitution(String educationInstitution) {
            this.educationInstitution = educationInstitution;
            return this;
        }

        public Builder educationBranch(String educationBranch) {
            this.educationBranch = educationBranch;
            return this;
        }

        public Builder educationPassingYear(String educationPassingYear) {
            this.educationPassingYear = educationPassingYear;
            return this;
        }

        public Builder skills(String skills) {
            this.skills = skills;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(token, refreshToken, email, role, userId, employeeId, fullName, phone, profilePhoto, bio, educationDegree, educationInstitution, educationBranch, educationPassingYear, skills, address);
        }
    }
}
