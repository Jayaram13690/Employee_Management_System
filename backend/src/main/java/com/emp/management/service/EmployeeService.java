package com.emp.management.service;

import com.emp.management.dto.request.EmployeeRequest;
import com.emp.management.dto.response.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for Employee CRUD operations.
 */
public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest request, String email, String password);

    Page<EmployeeResponse> getAllEmployees(Pageable pageable);

    Page<EmployeeResponse> searchEmployees(String search, Pageable pageable);

    EmployeeResponse getEmployeeById(Long id);

    EmployeeResponse getEmployeeByUserId(Long userId);

    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

    void deleteEmployee(Long id);

    EmployeeResponse uploadProfilePhoto(Long id, MultipartFile file);

    EmployeeResponse updateMyProfile(Long userId, com.emp.management.dto.request.ProfileUpdateRequest request);

    EmployeeResponse updateMyProfilePhoto(Long userId, String avatarUrl);

    void changePassword(Long userId, com.emp.management.dto.request.ChangePasswordRequest request);
}
