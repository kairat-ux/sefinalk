package com.example.demo.service;

import com.example.demo.dto.request.UserLoginRequestDTO;
import com.example.demo.dto.request.UserRegistrationRequestDTO;
import com.example.demo.dto.response.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO register(UserRegistrationRequestDTO request);
    AuthResponseDTO login(UserLoginRequestDTO request);
    void logout(String token);
    AuthResponseDTO refreshToken(String refreshToken);
}
