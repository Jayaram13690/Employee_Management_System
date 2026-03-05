package com.emp.management.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for employee response data.
 */
public class EmployeeResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String designation;
    private LocalDate dateOfJoining;
    private Double salary;
    private boolean active;
    private String profilePhoto;
    private String bio;
    private String educationDegree;
    private String educationInstitution;
    private String educationBranch;
    private String educationPassingYear;
    private String skills;
    private String address;

    // Department info
    private Long departmentId;
    private String departmentName;

    // Audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ===== Constructors =====

    public EmployeeResponse() {
    }

    public EmployeeResponse(Long id, String firstName, String lastName, String email, String phone, String designation, LocalDate dateOfJoining, Double salary, boolean active, String profilePhoto, String bio, String educationDegree, String educationInstitution, String educationBranch, String educationPassingYear, String skills, String address, Long departmentId, String departmentName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.designation = designation;
        this.dateOfJoining = dateOfJoining;
        this.salary = salary;
        this.active = active;
        this.profilePhoto = profilePhoto;
        this.bio = bio;
        this.educationDegree = educationDegree;
        this.educationInstitution = educationInstitution;
        this.educationBranch = educationBranch;
        this.educationPassingYear = educationPassingYear;
        this.skills = skills;
        this.address = address;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ===== Getters and Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ===== Builder Pattern Support =====

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String designation;
        private LocalDate dateOfJoining;
        private Double salary;
        private boolean active;
        private String profilePhoto;
        private String bio;
        private String educationDegree;
        private String educationInstitution;
        private String educationBranch;
        private String educationPassingYear;
        private String skills;
        private String address;
        private Long departmentId;
        private String departmentName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder designation(String designation) {
            this.designation = designation;
            return this;
        }

        public Builder dateOfJoining(LocalDate dateOfJoining) {
            this.dateOfJoining = dateOfJoining;
            return this;
        }

        public Builder salary(Double salary) {
            this.salary = salary;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
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

        public Builder departmentId(Long departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public Builder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public EmployeeResponse build() {
            return new EmployeeResponse(id, firstName, lastName, email, phone, designation, dateOfJoining, salary, active, profilePhoto, bio, educationDegree, educationInstitution, educationBranch, educationPassingYear, skills, address, departmentId, departmentName, createdAt, updatedAt);
        }
    }
}
