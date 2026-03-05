package com.emp.management.entity;

import com.emp.management.enums.LeaveType;
import jakarta.persistence.*;

/**
 * Leave balance tracking entity.
 * Tracks available, used, and remaining leave days for each leave type per employee per year.
 */
@Entity
@Table(name = "leave_balance",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"employee_id", "leave_type", "year"})
        },
        indexes = {
                @Index(name = "idx_employee_id_year", columnList = "employee_id,year"),
                @Index(name = "idx_employee_leave_year", columnList = "employee_id,leave_type,year")
        })
public class LeaveBalance {

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
    private int year;

    @Column(nullable = false)
    private int allocatedDays;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int usedDays;

    // ===== Constructors =====

    public LeaveBalance() {
    }

    public LeaveBalance(Long id, Employee employee, LeaveType leaveType, int year, int allocatedDays, int usedDays) {
        this.id = id;
        this.employee = employee;
        this.leaveType = leaveType;
        this.year = year;
        this.allocatedDays = allocatedDays;
        this.usedDays = usedDays;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAllocatedDays() {
        return allocatedDays;
    }

    public void setAllocatedDays(int allocatedDays) {
        this.allocatedDays = allocatedDays;
    }

    public int getUsedDays() {
        return usedDays;
    }

    public void setUsedDays(int usedDays) {
        this.usedDays = usedDays;
    }

    /**
     * Calculate remaining days based on allocated - used.
     */
    public int getRemainingDays() {
        return allocatedDays - usedDays;
    }

    /**
     * Check if employee has enough balance for requested days.
     */
    public boolean hasBalance(int requestedDays) {
        return getRemainingDays() >= requestedDays;
    }

    /**
     * Add used days when leave is approved.
     */
    public void addUsedDays(int days) {
        if (!hasBalance(days)) {
            throw new IllegalStateException("Insufficient leave balance");
        }
        this.usedDays += days;
    }

    /**
     * Remove used days when leave is rejected.
     */
    public void removeUsedDays(int days) {
        this.usedDays = Math.max(0, this.usedDays - days);
    }
}
