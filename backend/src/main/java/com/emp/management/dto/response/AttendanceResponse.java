package com.emp.management.dto.response;

import com.emp.management.enums.AttendanceStatus;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for attendance response data.
 */
public class AttendanceResponse {

    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate date;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private AttendanceStatus status;

    // ===== Constructors =====

    public AttendanceResponse() {
    }

    public AttendanceResponse(Long id, Long employeeId, String employeeName, LocalDate date, LocalTime checkIn, LocalTime checkOut, AttendanceStatus status) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.date = date;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }

    // ===== Getters and Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalTime checkOut) {
        this.checkOut = checkOut;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    // ===== Builder Pattern Support =====

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long employeeId;
        private String employeeName;
        private LocalDate date;
        private LocalTime checkIn;
        private LocalTime checkOut;
        private AttendanceStatus status;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder employeeId(Long employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder employeeName(String employeeName) {
            this.employeeName = employeeName;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder checkIn(LocalTime checkIn) {
            this.checkIn = checkIn;
            return this;
        }

        public Builder checkOut(LocalTime checkOut) {
            this.checkOut = checkOut;
            return this;
        }

        public Builder status(AttendanceStatus status) {
            this.status = status;
            return this;
        }

        public AttendanceResponse build() {
            return new AttendanceResponse(id, employeeId, employeeName, date, checkIn, checkOut, status);
        }
    }
}
