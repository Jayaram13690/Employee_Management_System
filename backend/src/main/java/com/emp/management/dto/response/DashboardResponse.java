package com.emp.management.dto.response;

/**
 * DTO for dashboard summary data (admin view).
 */
public class DashboardResponse {

    // Employee stats
    private long totalEmployees;
    private long activeEmployees;
    private long inactiveEmployees;

    // Today's attendance
    private long todayPresent;
    private long todayAbsent;
    private long todayOnLeave;

    // Leave stats
    private long pendingLeaveRequests;

    // ===== Constructors =====

    public DashboardResponse() {
    }

    public DashboardResponse(long totalEmployees, long activeEmployees, long inactiveEmployees, long todayPresent, long todayAbsent, long todayOnLeave, long pendingLeaveRequests) {
        this.totalEmployees = totalEmployees;
        this.activeEmployees = activeEmployees;
        this.inactiveEmployees = inactiveEmployees;
        this.todayPresent = todayPresent;
        this.todayAbsent = todayAbsent;
        this.todayOnLeave = todayOnLeave;
        this.pendingLeaveRequests = pendingLeaveRequests;
    }

    // ===== Getters and Setters =====

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public long getActiveEmployees() {
        return activeEmployees;
    }

    public void setActiveEmployees(long activeEmployees) {
        this.activeEmployees = activeEmployees;
    }

    public long getInactiveEmployees() {
        return inactiveEmployees;
    }

    public void setInactiveEmployees(long inactiveEmployees) {
        this.inactiveEmployees = inactiveEmployees;
    }

    public long getTodayPresent() {
        return todayPresent;
    }

    public void setTodayPresent(long todayPresent) {
        this.todayPresent = todayPresent;
    }

    public long getTodayAbsent() {
        return todayAbsent;
    }

    public void setTodayAbsent(long todayAbsent) {
        this.todayAbsent = todayAbsent;
    }

    public long getTodayOnLeave() {
        return todayOnLeave;
    }

    public void setTodayOnLeave(long todayOnLeave) {
        this.todayOnLeave = todayOnLeave;
    }

    public long getPendingLeaveRequests() {
        return pendingLeaveRequests;
    }

    public void setPendingLeaveRequests(long pendingLeaveRequests) {
        this.pendingLeaveRequests = pendingLeaveRequests;
    }

    // ===== Builder Pattern Support =====

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long totalEmployees;
        private long activeEmployees;
        private long inactiveEmployees;
        private long todayPresent;
        private long todayAbsent;
        private long todayOnLeave;
        private long pendingLeaveRequests;

        public Builder totalEmployees(long totalEmployees) {
            this.totalEmployees = totalEmployees;
            return this;
        }

        public Builder activeEmployees(long activeEmployees) {
            this.activeEmployees = activeEmployees;
            return this;
        }

        public Builder inactiveEmployees(long inactiveEmployees) {
            this.inactiveEmployees = inactiveEmployees;
            return this;
        }

        public Builder todayPresent(long todayPresent) {
            this.todayPresent = todayPresent;
            return this;
        }

        public Builder todayAbsent(long todayAbsent) {
            this.todayAbsent = todayAbsent;
            return this;
        }

        public Builder todayOnLeave(long todayOnLeave) {
            this.todayOnLeave = todayOnLeave;
            return this;
        }

        public Builder pendingLeaveRequests(long pendingLeaveRequests) {
            this.pendingLeaveRequests = pendingLeaveRequests;
            return this;
        }

        public DashboardResponse build() {
            return new DashboardResponse(totalEmployees, activeEmployees, inactiveEmployees, todayPresent, todayAbsent, todayOnLeave, pendingLeaveRequests);
        }
    }
}
