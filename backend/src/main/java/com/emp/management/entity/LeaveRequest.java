package com.emp.management.entity;

import com.emp.management.enums.LeaveStatus;
import com.emp.management.enums.LeaveType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * LeaveRequest entity — employee leave application with admin approval
 * workflow.
 */
@Entity
@Table(name = "leave_requests", indexes = {
        @Index(name = "idx_employee_id", columnList = "employee_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType leaveType;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status = LeaveStatus.PENDING;

    private String adminRemarks;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ===== Constructors =====

    public LeaveRequest() {
    }

    public LeaveRequest(Long id, Employee employee, LeaveType leaveType, LocalDate startDate, LocalDate endDate, String reason, LeaveStatus status, String adminRemarks, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.employee = employee;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
        this.adminRemarks = adminRemarks;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public String getAdminRemarks() {
        return adminRemarks;
    }

    public void setAdminRemarks(String adminRemarks) {
        this.adminRemarks = adminRemarks;
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
     * Calculate the number of leave days (working days only, excluding weekends).
     * Monday = 1, Tuesday = 2, ... Friday = 5, Saturday = 6, Sunday = 7
     */
    public long getLeaveDays() {
        long workingDays = 0;
        LocalDate current = startDate;

        // Count only weekdays (Monday-Friday)
        while (!current.isAfter(endDate)) {
            int dayOfWeek = current.getDayOfWeek().getValue();
            // Monday=1, Tuesday=2, Wednesday=3, Thursday=4, Friday=5
            // Saturday=6, Sunday=7
            if (dayOfWeek >= 1 && dayOfWeek <= 5) {
                workingDays++;
            }
            current = current.plusDays(1);
        }

        return workingDays;
    }
}
