package com.filesharepro.service;

import com.filesharepro.dto.JwtResponse;
import com.filesharepro.dto.LoginRequest;
import com.filesharepro.dto.SignupRequest;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    JwtResponse registerUser(SignupRequest signupRequest);
}