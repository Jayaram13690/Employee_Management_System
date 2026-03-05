package com.emp.management.service.impl;

import com.emp.management.dto.request.LeaveActionRequest;
import com.emp.management.dto.request.LeaveRequestDTO;
import com.emp.management.dto.response.LeaveResponse;
import com.emp.management.entity.Employee;
import com.emp.management.entity.LeaveRequest;
import com.emp.management.enums.LeaveStatus;
import com.emp.management.exception.BadRequestException;
import com.emp.management.exception.ResourceNotFoundException;
import com.emp.management.repository.EmployeeRepository;
import com.emp.management.repository.LeaveRequestRepository;
import com.emp.management.service.EmailService;
import com.emp.management.service.LeaveBalanceService;
import com.emp.management.service.LeaveService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Leave service implementation — handles leave application,
 * admin approval/rejection, and leave history queries.
 */
@Service
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;
    private final LeaveBalanceService leaveBalanceService;

    // ===== Constructor Injection =====

    public LeaveServiceImpl(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository, EmailService emailService, LeaveBalanceService leaveBalanceService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
        this.leaveBalanceService = leaveBalanceService;
    }

    @Override
    @Transactional
    public LeaveResponse applyLeave(Long employeeId, LeaveRequestDTO request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        // Validate date range
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date must be after start date");
        }

        LeaveRequest leaveRequest = new LeaveRequest(null, employee, request.getLeaveType(), request.getStartDate(), request.getEndDate(), request.getReason(), LeaveStatus.PENDING, null, null, null);

        leaveRequest = leaveRequestRepository.save(leaveRequest);
        System.out.println("Leave applied by " + employee.getFullName() + " — " + request.getStartDate() + " to " + request.getEndDate() + " (" + leaveRequest.getLeaveDays() + " days)");

        // Send application confirmation email
        emailService.sendLeaveApplicationEmail(
                employee.getUser().getEmail(),
                employee.getFullName(),
                request.getLeaveType().toString(),
                request.getStartDate().toString(),
                request.getEndDate().toString(),
                (int) leaveRequest.getLeaveDays()
        );

        return mapToResponse(leaveRequest);
    }

    @Override
    @Transactional
    public LeaveResponse approveLeave(Long leaveId, LeaveActionRequest actionRequest) {
        LeaveRequest leaveRequest = findLeaveOrThrow(leaveId);

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Leave request is already " + leaveRequest.getStatus());
        }

        leaveRequest.setStatus(LeaveStatus.APPROVED);
        leaveRequest.setAdminRemarks(actionRequest.getAdminRemarks());
        leaveRequest = leaveRequestRepository.save(leaveRequest);

        // Update leave balance
        Employee employee = leaveRequest.getEmployee();
        int year = Year.now().getValue();
        leaveBalanceService.updateUsedDays(
                employee.getId(),
                leaveRequest.getLeaveType(),
                (int) leaveRequest.getLeaveDays(),
                year
        );

        // Send approval email
        emailService.sendLeaveStatusEmail(
                employee.getUser().getEmail(),
                employee.getFullName(),
                LeaveStatus.APPROVED,
                leaveRequest.getLeaveType().toString(),
                leaveRequest.getStartDate().toString(),
                leaveRequest.getEndDate().toString(),
                actionRequest.getAdminRemarks()
        );

        System.out.println("Leave ID " + leaveId + " approved for " + employee.getFullName());

        return mapToResponse(leaveRequest);
    }

    @Override
    @Transactional
    public LeaveResponse rejectLeave(Long leaveId, LeaveActionRequest actionRequest) {
        LeaveRequest leaveRequest = findLeaveOrThrow(leaveId);

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Leave request is already " + leaveRequest.getStatus());
        }

        leaveRequest.setStatus(LeaveStatus.REJECTED);
        leaveRequest.setAdminRemarks(actionRequest.getAdminRemarks());
        leaveRequest = leaveRequestRepository.save(leaveRequest);

        // Send rejection email
        Employee employee = leaveRequest.getEmployee();
        emailService.sendLeaveStatusEmail(
                employee.getUser().getEmail(),
                employee.getFullName(),
                LeaveStatus.REJECTED,
                leaveRequest.getLeaveType().toString(),
                leaveRequest.getStartDate().toString(),
                leaveRequest.getEndDate().toString(),
                actionRequest.getAdminRemarks()
        );

        System.out.println("Leave ID " + leaveId + " rejected for " + employee.getFullName());

        return mapToResponse(leaveRequest);
    }

    @Override
    public Page<LeaveResponse> getLeavesByEmployee(Long employeeId, Pageable pageable) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        return leaveRequestRepository.findByEmployeeId(employeeId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<LeaveResponse> getPendingLeaves(Pageable pageable) {
        return leaveRequestRepository.findByStatus(LeaveStatus.PENDING, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<LeaveResponse> getAllLeaves(Pageable pageable) {
        return leaveRequestRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public LeaveResponse getLeaveById(Long leaveId) {
        return mapToResponse(findLeaveOrThrow(leaveId));
    }

    // ===== Helper Methods =====

    private LeaveRequest findLeaveOrThrow(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveRequest", "id", id));
    }

    private LeaveResponse mapToResponse(LeaveRequest leave) {
        return LeaveResponse.builder()
                .id(leave.getId())
                .employeeId(leave.getEmployee().getId())
                .employeeName(leave.getEmployee().getFullName())
                .leaveType(leave.getLeaveType())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .leaveDays(leave.getLeaveDays())
                .reason(leave.getReason())
                .status(leave.getStatus())
                .adminRemarks(leave.getAdminRemarks())
                .createdAt(leave.getCreatedAt())
                .updatedAt(leave.getUpdatedAt())
                .build();
    }
}
