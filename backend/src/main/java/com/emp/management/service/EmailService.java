package com.emp.management.service;

import com.emp.management.enums.LeaveStatus;

/**
 * Email service interface for sending notifications.
 */
public interface EmailService {

    /**
     * Send welcome email on user registration.
     */
    void sendWelcomeEmail(String email, String fullName);

    /**
     * Send leave approval/rejection notification.
     */
    void sendLeaveStatusEmail(String email, String employeeName, LeaveStatus status,
                             String leaveType, String startDate, String endDate, String remarks);

    /**
     * Send password reset email (future feature).
     */
    void sendPasswordResetEmail(String email, String resetLink);

    /**
     * Send leave application confirmation.
     */
    void sendLeaveApplicationEmail(String email, String employeeName, String leaveType,
                                   String startDate, String endDate, int days);
}
