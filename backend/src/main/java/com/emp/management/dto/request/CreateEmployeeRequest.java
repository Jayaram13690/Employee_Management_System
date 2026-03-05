package com.emp.management.dto.request;

import com.emp.management.validation.ValidPassword;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.DecimalMax;

import java.time.LocalDate;

/**
 * DTO for creating a new employee (includes user credentials).
 */
public class CreateEmployeeRequest {

    // User credentials
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;

    // Employee details
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

    // ===== Constructors =====

    public CreateEmployeeRequest() {
    }

    public CreateEmployeeRequest(String email, String password, String firstName, String lastName, String phone, String designation, LocalDate dateOfJoining, Double salary, Long departmentId, boolean active) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.designation = designation;
        this.dateOfJoining = dateOfJoining;
        this.salary = salary;
        this.departmentId = departmentId;
        this.active = active;
    }

    // ===== Getters and Setters =====

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
