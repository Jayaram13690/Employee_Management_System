package com.emp.management.repository;

import com.emp.management.entity.LeaveRequest;
import com.emp.management.enums.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for LeaveRequest entity with status-based filtering.
 */
@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);

    List<LeaveRequest> findByStatusOrderByCreatedAtDesc(LeaveStatus status);

    List<LeaveRequest> findByEmployeeIdAndStatus(Long employeeId, LeaveStatus status);

    long countByStatus(LeaveStatus status);

    long countByEmployeeIdAndStatus(Long employeeId, LeaveStatus status);

    Page<LeaveRequest> findByEmployeeId(Long employeeId, Pageable pageable);

    Page<LeaveRequest> findByStatus(LeaveStatus status, Pageable pageable);
}
