package com.emp.management.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Employee entity — stores employee profile information.
 * Linked to a User (for auth) and a Department.
 */
@Entity
@Table(name = "employees", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_department_id", columnList = "department_id"),
        @Index(name = "idx_phone", columnList = "phone", unique = true),
        @Index(name = "idx_active", columnList = "active")
})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String phone;

    private String designation;

    private LocalDate dateOfJoining;

    private Double salary;

    private String profilePhoto;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String educationDegree;
    private String educationInstitution;
    private String educationBranch;
    private String educationPassingYear;

    private String skills;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false)
    private boolean active = true;

    // ===== Relationships =====

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    // ===== Audit Fields =====

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ===== Constructors =====

    public Employee() {
    }

    public Employee(Long id, String firstName, String lastName, String phone, String designation, LocalDate dateOfJoining, Double salary, String profilePhoto, String bio, String educationDegree, String educationInstitution, String educationBranch, String educationPassingYear, String skills, String address, boolean active, User user, Department department, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.designation = designation;
        this.dateOfJoining = dateOfJoining;
        this.salary = salary;
        this.profilePhoto = profilePhoto;
        this.bio = bio;
        this.educationDegree = educationDegree;
        this.educationInstitution = educationInstitution;
        this.educationBranch = educationBranch;
        this.educationPassingYear = educationPassingYear;
        this.skills = skills;
        this.address = address;
        this.active = active;
        this.user = user;
        this.department = department;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

    /**
     * Convenience method to get full name.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
