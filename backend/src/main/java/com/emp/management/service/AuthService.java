package com.emp.management.service;

import com.emp.management.dto.request.LoginRequest;
import com.emp.management.dto.request.RegisterRequest;
import com.emp.management.dto.request.RefreshTokenRequest;
import com.emp.management.dto.response.AuthResponse;

/**
 * Service interface for authentication operations.
 */
public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout(String refreshToken);
}
