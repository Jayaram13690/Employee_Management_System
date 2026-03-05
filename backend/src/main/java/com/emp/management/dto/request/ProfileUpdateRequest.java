package com.emp.management.dto.request;

/**
 * DTO for self-updating employee profile details.
 */
public class ProfileUpdateRequest {
    private String phone;
    private String bio;
    private String educationDegree;
    private String educationInstitution;
    private String educationBranch;
    private String educationPassingYear;
    private String address;
    private String skills;

    // ===== Constructors =====

    public ProfileUpdateRequest() {
    }

    public ProfileUpdateRequest(String phone, String bio, String educationDegree, String educationInstitution, String educationBranch, String educationPassingYear, String address, String skills) {
        this.phone = phone;
        this.bio = bio;
        this.educationDegree = educationDegree;
        this.educationInstitution = educationInstitution;
        this.educationBranch = educationBranch;
        this.educationPassingYear = educationPassingYear;
        this.address = address;
        this.skills = skills;
    }

    // ===== Getters and Setters =====

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}
