package com.emp.management.dto.response;

import com.emp.management.enums.LeaveType;

/**
 * DTO for leave balance response.
 */
public class LeaveBalanceResponse {

    private Long employeeId;
    private LeaveType leaveType;
    private int year;
    private int allocatedDays;
    private int usedDays;
    private int remainingDays;

    // ===== Constructors =====

    public LeaveBalanceResponse() {
    }

    public LeaveBalanceResponse(Long employeeId, LeaveType leaveType, int year, int allocatedDays, int usedDays, int remainingDays) {
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.year = year;
        this.allocatedDays = allocatedDays;
        this.usedDays = usedDays;
        this.remainingDays = remainingDays;
    }

    // ===== Getters and Setters =====

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    public int getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(int remainingDays) {
        this.remainingDays = remainingDays;
    }

    // ===== Builder Pattern Support =====

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long employeeId;
        private LeaveType leaveType;
        private int year;
        private int allocatedDays;
        private int usedDays;
        private int remainingDays;

        public Builder employeeId(Long employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder leaveType(LeaveType leaveType) {
            this.leaveType = leaveType;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder allocatedDays(int allocatedDays) {
            this.allocatedDays = allocatedDays;
            return this;
        }

        public Builder usedDays(int usedDays) {
            this.usedDays = usedDays;
            return this;
        }

        public Builder remainingDays(int remainingDays) {
            this.remainingDays = remainingDays;
            return this;
        }

        public LeaveBalanceResponse build() {
            return new LeaveBalanceResponse(employeeId, leaveType, year, allocatedDays, usedDays, remainingDays);
        }
    }
}
