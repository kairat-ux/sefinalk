// ПУТЬ: src/main/java/com/example/demo/controller/AuthController.java

package com.example.demo.controller;

import com.example.demo.dto.request.UserLoginRequestDTO;
import com.example.demo.dto.request.UserRegistrationRequestDTO;
import com.example.demo.dto.response.AuthResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody UserRegistrationRequestDTO request) {
        UserResponseDTO user = userService.register(request);

        AuthResponseDTO response = AuthResponseDTO.builder()
                .token("jwt-token-" + System.currentTimeMillis())
                .tokenType("Bearer")
                .expiresIn(86400000L)
                .user(user)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO request) {
        UserResponseDTO user = userService.getUserByEmail(request.getEmail());

        AuthResponseDTO response = AuthResponseDTO.builder()
                .token("jwt-token-" + System.currentTimeMillis())
                .tokenType("Bearer")
                .expiresIn(86400000L)
                .user(user)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        // Будет работать после интеграции Spring Security
        return ResponseEntity.ok(null);
    }
}