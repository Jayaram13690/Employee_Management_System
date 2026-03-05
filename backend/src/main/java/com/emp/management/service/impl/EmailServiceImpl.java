package com.emp.management.service.impl;

import com.emp.management.enums.LeaveStatus;
import com.emp.management.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Email service implementation using Spring Mail.
 * Sends emails asynchronously to avoid blocking requests.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.from-name}")
    private String fromName;

    // ===== Constructor Injection =====

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendWelcomeEmail(String email, String fullName) {
        try {
            String subject = "Welcome to Employee Management System";
            String content = buildWelcomeEmailContent(fullName);
            sendEmail(email, subject, content);
            System.out.println("Welcome email sent to " + email);
        } catch (Exception e) {
            System.out.println("Failed to send welcome email to " + email + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void sendLeaveStatusEmail(String email, String employeeName, LeaveStatus status,
                                     String leaveType, String startDate, String endDate, String remarks) {
        try {
            String subject = "Leave Request " + status + " - " + leaveType;
            String content = buildLeaveStatusEmailContent(employeeName, status, leaveType, startDate, endDate, remarks);
            sendEmail(email, subject, content);
            System.out.println("Leave status email (" + status + ") sent to " + email);
        } catch (Exception e) {
            System.out.println("Failed to send leave status email to " + email + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String email, String resetLink) {
        try {
            String subject = "Password Reset Request";
            String content = buildPasswordResetEmailContent(resetLink);
            sendEmail(email, subject, content);
            System.out.println("Password reset email sent to " + email);
        } catch (Exception e) {
            System.out.println("Failed to send password reset email to " + email + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void sendLeaveApplicationEmail(String email, String employeeName, String leaveType,
                                          String startDate, String endDate, int days) {
        try {
            String subject = "Leave Application Confirmation - " + leaveType;
            String content = buildLeaveApplicationEmailContent(employeeName, leaveType, startDate, endDate, days);
            sendEmail(email, subject, content);
            System.out.println("Leave application confirmation email sent to " + email);
        } catch (Exception e) {
            System.out.println("Failed to send leave application email to " + email + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===== Helper Methods =====

    private void sendEmail(String to, String subject, String content) throws MessagingException, java.io.UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail, fromName);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true); // true for HTML content

        mailSender.send(message);
    }

    private String buildWelcomeEmailContent(String fullName) {
        return "<html><head><style>" +
                "body { font-family: Arial, sans-serif; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #2c3e50; color: white; padding: 20px; text-align: center; }" +
                ".content { padding: 20px; background-color: #ecf0f1; }" +
                ".footer { text-align: center; padding: 10px; color: #7f8c8d; font-size: 12px; }" +
                "</style></head><body>" +
                "<div class='container'>" +
                "<div class='header'><h1>Welcome to Employee Management System</h1></div>" +
                "<div class='content'>" +
                "<p>Hello <strong>" + fullName + "</strong>,</p>" +
                "<p>We're excited to have you on board! Your account has been successfully created.</p>" +
                "<p>You can now login to the Employee Management System and manage your:</p>" +
                "<ul>" +
                "<li>Employee Profile</li>" +
                "<li>Attendance Records</li>" +
                "<li>Leave Requests</li>" +
                "<li>Personal Information</li>" +
                "</ul>" +
                "<p>If you have any questions, please reach out to your HR department.</p>" +
                "<p>Best regards,<br><strong>Employee Management Team</strong></p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2024 Employee Management System. All rights reserved.</p>" +
                "</div></div></body></html>";
    }

    private String buildLeaveStatusEmailContent(String employeeName, LeaveStatus status, String leaveType,
                                                String startDate, String endDate, String remarks) {
        String statusColor = status == LeaveStatus.APPROVED ? "#27ae60" : "#e74c3c";
        String statusText = status.toString();

        String remarksHtml = (remarks != null && !remarks.isEmpty()) ?
                "<div class='detail-row'><span class='label'>Admin Remarks:</span> " + remarks + "</div>" : "";

        return "<html><head><style>" +
                "body { font-family: Arial, sans-serif; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #2c3e50; color: white; padding: 20px; text-align: center; }" +
                ".status-badge { background-color: " + statusColor + "; color: white; padding: 10px 20px; text-align: center; font-weight: bold; font-size: 18px; border-radius: 5px; }" +
                ".content { padding: 20px; background-color: #ecf0f1; }" +
                ".detail-row { margin: 10px 0; }" +
                ".label { font-weight: bold; color: #2c3e50; }" +
                ".footer { text-align: center; padding: 10px; color: #7f8c8d; font-size: 12px; }" +
                "</style></head><body>" +
                "<div class='container'>" +
                "<div class='header'><h1>Leave Request Status</h1></div>" +
                "<div class='status-badge'>" + statusText + "</div>" +
                "<div class='content'>" +
                "<p>Dear <strong>" + employeeName + "</strong>,</p>" +
                "<p>Your leave request has been <strong>" + statusText + "</strong>.</p>" +
                "<div class='detail-row'><span class='label'>Leave Type:</span> " + leaveType + "</div>" +
                "<div class='detail-row'><span class='label'>Start Date:</span> " + startDate + "</div>" +
                "<div class='detail-row'><span class='label'>End Date:</span> " + endDate + "</div>" +
                remarksHtml +
                "<p>If you have any questions, please contact your HR department.</p>" +
                "<p>Best regards,<br><strong>Employee Management Team</strong></p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2024 Employee Management System. All rights reserved.</p>" +
                "</div></div></body></html>";
    }

    private String buildLeaveApplicationEmailContent(String employeeName, String leaveType,
                                                     String startDate, String endDate, int days) {
        return "<html><head><style>" +
                "body { font-family: Arial, sans-serif; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #2c3e50; color: white; padding: 20px; text-align: center; }" +
                ".content { padding: 20px; background-color: #ecf0f1; }" +
                ".detail-row { margin: 10px 0; }" +
                ".label { font-weight: bold; color: #2c3e50; }" +
                ".footer { text-align: center; padding: 10px; color: #7f8c8d; font-size: 12px; }" +
                "</style></head><body>" +
                "<div class='container'>" +
                "<div class='header'><h1>Leave Application Submitted</h1></div>" +
                "<div class='content'>" +
                "<p>Dear <strong>" + employeeName + "</strong>,</p>" +
                "<p>Your leave application has been successfully submitted and is pending approval.</p>" +
                "<div class='detail-row'><span class='label'>Leave Type:</span> " + leaveType + "</div>" +
                "<div class='detail-row'><span class='label'>Start Date:</span> " + startDate + "</div>" +
                "<div class='detail-row'><span class='label'>End Date:</span> " + endDate + "</div>" +
                "<div class='detail-row'><span class='label'>Number of Days:</span> " + days + "</div>" +
                "<p>Your HR administrator will review and approve/reject your application shortly.</p>" +
                "<p>Best regards,<br><strong>Employee Management Team</strong></p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2024 Employee Management System. All rights reserved.</p>" +
                "</div></div></body></html>";
    }

    private String buildPasswordResetEmailContent(String resetLink) {
        return "<html><head><style>" +
                "body { font-family: Arial, sans-serif; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #2c3e50; color: white; padding: 20px; text-align: center; }" +
                ".content { padding: 20px; background-color: #ecf0f1; }" +
                ".button { background-color: #3498db; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; }" +
                ".footer { text-align: center; padding: 10px; color: #7f8c8d; font-size: 12px; }" +
                "</style></head><body>" +
                "<div class='container'>" +
                "<div class='header'><h1>Password Reset Request</h1></div>" +
                "<div class='content'>" +
                "<p>You have requested to reset your password.</p>" +
                "<p>Click the button below to reset your password:</p>" +
                "<p><a href='" + resetLink + "' class='button'>Reset Password</a></p>" +
                "<p>This link will expire in 24 hours.</p>" +
                "<p>If you did not request a password reset, please ignore this email.</p>" +
                "<p>Best regards,<br><strong>Employee Management Team</strong></p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2024 Employee Management System. All rights reserved.</p>" +
                "</div></div></body></html>";
    }
}
