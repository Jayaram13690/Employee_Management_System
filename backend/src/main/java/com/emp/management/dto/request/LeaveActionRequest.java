package com.emp.management.dto.request;

/**
 * DTO for admin leave approval/rejection.
 */
public class LeaveActionRequest {

    private String adminRemarks;

    // ===== Constructors =====

    public LeaveActionRequest() {
    }

    public LeaveActionRequest(String adminRemarks) {
        this.adminRemarks = adminRemarks;
    }

    // ===== Getters and Setters =====

    public String getAdminRemarks() {
        return adminRemarks;
    }

    public void setAdminRemarks(String adminRemarks) {
        this.adminRemarks = adminRemarks;
    }
}
