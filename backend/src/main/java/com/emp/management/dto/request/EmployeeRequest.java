package com.emp.management.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * DTO for creating/updating employee records.
 */
public class EmployeeRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "First name can only contain letters, spaces, hyphens, and apostrophes")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "Last name can only contain letters, spaces, hyphens, and apostrophes")
    private String lastName;

    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be 10-15 digits and may start with +")
    private String phone;

    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designation;

    @PastOrPresent(message = "Date of joining must be today or in the past")
    private LocalDate dateOfJoining;

    @Positive(message = "Salary must be positive")
    @DecimalMax(value = "10000000.00", message = "Salary cannot exceed 10,000,000")
    private Double salary;

    private Long departmentId;

    private boolean active = true;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    @Size(max = 100, message = "Education degree must not exceed 100 characters")
    private String educationDegree;

    @Size(max = 200, message = "Education institution must not exceed 200 characters")
    private String educationInstitution;

    @Size(max = 100, message = "Education branch must not exceed 100 characters")
    private String educationBranch;

    @Size(max = 4, message = "Education passing year must not exceed 4 characters")
    private String educationPassingYear;

    @Size(max = 500, message = "Skills must not exceed 500 characters")
    private String skills;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    // ===== Constructors =====

    public EmployeeRequest() {
    }

    public EmployeeRequest(String firstName, String lastName, String phone, String designation, LocalDate dateOfJoining, Double salary, Long departmentId, boolean active, String bio, String educationDegree, String educationInstitution, String educationBranch, String educationPassingYear, String skills, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.designation = designation;
        this.dateOfJoining = dateOfJoining;
        this.salary = salary;
        this.departmentId = departmentId;
        this.active = active;
        this.bio = bio;
        this.educationDegree = educationDegree;
        this.educationInstitution = educationInstitution;
        this.educationBranch = educationBranch;
        this.educationPassingYear = educationPassingYear;
        this.skills = skills;
        this.address = address;
    }

    // ===== Getters and Setters =====

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
}
