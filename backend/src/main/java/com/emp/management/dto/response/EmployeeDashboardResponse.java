package com.emp.management.dto.response;

import java.util.List;

/**
 * DTO for employee dashboard view.
 */
public class EmployeeDashboardResponse {

    // Attendance summary
    private long totalPresent;
    private long totalAbsent;
    private long totalLate;
    private long totalHalfDay;

    // Leave summary
    private long totalLeavesApplied;
    private long pendingLeaves;
    private long approvedLeaves;
    private long rejectedLeaves;

    // Recent leaves
    private List<LeaveResponse> recentLeaves;

    // ===== Constructors =====

    public EmployeeDashboardResponse() {
    }

    public EmployeeDashboardResponse(long totalPresent, long totalAbsent, long totalLate, long totalHalfDay, long totalLeavesApplied, long pendingLeaves, long approvedLeaves, long rejectedLeaves, List<LeaveResponse> recentLeaves) {
        this.totalPresent = totalPresent;
        this.totalAbsent = totalAbsent;
        this.totalLate = totalLate;
        this.totalHalfDay = totalHalfDay;
        this.totalLeavesApplied = totalLeavesApplied;
        this.pendingLeaves = pendingLeaves;
        this.approvedLeaves = approvedLeaves;
        this.rejectedLeaves = rejectedLeaves;
        this.recentLeaves = recentLeaves;
    }

    // ===== Getters and Setters =====

    public long getTotalPresent() {
        return totalPresent;
    }

    public void setTotalPresent(long totalPresent) {
        this.totalPresent = totalPresent;
    }

    public long getTotalAbsent() {
        return totalAbsent;
    }

    public void setTotalAbsent(long totalAbsent) {
        this.totalAbsent = totalAbsent;
    }

    public long getTotalLate() {
        return totalLate;
    }

    public void setTotalLate(long totalLate) {
        this.totalLate = totalLate;
    }

    public long getTotalHalfDay() {
        return totalHalfDay;
    }

    public void setTotalHalfDay(long totalHalfDay) {
        this.totalHalfDay = totalHalfDay;
    }

    public long getTotalLeavesApplied() {
        return totalLeavesApplied;
    }

    public void setTotalLeavesApplied(long totalLeavesApplied) {
        this.totalLeavesApplied = totalLeavesApplied;
    }

    public long getPendingLeaves() {
        return pendingLeaves;
    }

    public void setPendingLeaves(long pendingLeaves) {
        this.pendingLeaves = pendingLeaves;
    }

    public long getApprovedLeaves() {
        return approvedLeaves;
    }

    public void setApprovedLeaves(long approvedLeaves) {
        this.approvedLeaves = approvedLeaves;
    }

    public long getRejectedLeaves() {
        return rejectedLeaves;
    }

    public void setRejectedLeaves(long rejectedLeaves) {
        this.rejectedLeaves = rejectedLeaves;
    }

    public List<LeaveResponse> getRecentLeaves() {
        return recentLeaves;
    }

    public void setRecentLeaves(List<LeaveResponse> recentLeaves) {
        this.recentLeaves = recentLeaves;
    }

    // ===== Builder Pattern Support =====

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long totalPresent;
        private long totalAbsent;
        private long totalLate;
        private long totalHalfDay;
        private long totalLeavesApplied;
        private long pendingLeaves;
        private long approvedLeaves;
        private long rejectedLeaves;
        private List<LeaveResponse> recentLeaves;

        public Builder totalPresent(long totalPresent) {
            this.totalPresent = totalPresent;
            return this;
        }

        public Builder totalAbsent(long totalAbsent) {
            this.totalAbsent = totalAbsent;
            return this;
        }

        public Builder totalLate(long totalLate) {
            this.totalLate = totalLate;
            return this;
        }

        public Builder totalHalfDay(long totalHalfDay) {
            this.totalHalfDay = totalHalfDay;
            return this;
        }

        public Builder totalLeavesApplied(long totalLeavesApplied) {
            this.totalLeavesApplied = totalLeavesApplied;
            return this;
        }

        public Builder pendingLeaves(long pendingLeaves) {
            this.pendingLeaves = pendingLeaves;
            return this;
        }

        public Builder approvedLeaves(long approvedLeaves) {
            this.approvedLeaves = approvedLeaves;
            return this;
        }

        public Builder rejectedLeaves(long rejectedLeaves) {
            this.rejectedLeaves = rejectedLeaves;
            return this;
        }

        public Builder recentLeaves(List<LeaveResponse> recentLeaves) {
            this.recentLeaves = recentLeaves;
            return this;
        }

        public EmployeeDashboardResponse build() {
            return new EmployeeDashboardResponse(totalPresent, totalAbsent, totalLate, totalHalfDay, totalLeavesApplied, pendingLeaves, approvedLeaves, rejectedLeaves, recentLeaves);
        }
    }
}
