package com.emp.management.service;

import com.emp.management.dto.request.LeaveActionRequest;
import com.emp.management.dto.request.LeaveRequestDTO;
import com.emp.management.dto.response.LeaveResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for Leave management operations.
 */
public interface LeaveService {

    LeaveResponse applyLeave(Long employeeId, LeaveRequestDTO request);

    LeaveResponse approveLeave(Long leaveId, LeaveActionRequest actionRequest);

    LeaveResponse rejectLeave(Long leaveId, LeaveActionRequest actionRequest);

    Page<LeaveResponse> getLeavesByEmployee(Long employeeId, Pageable pageable);

    Page<LeaveResponse> getPendingLeaves(Pageable pageable);

    Page<LeaveResponse> getAllLeaves(Pageable pageable);

    LeaveResponse getLeaveById(Long leaveId);
}
