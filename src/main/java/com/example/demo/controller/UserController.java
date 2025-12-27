// ПУТЬ: src/main/java/com/example/demo/controller/UserController.java

package com.example.demo.controller;

import com.example.demo.dto.request.ChangePasswordRequestDTO;
import com.example.demo.dto.request.UserRegistrationRequestDTO;
import com.example.demo.dto.request.UserRoleUpdateRequestDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        UserResponseDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id,
                                           @Valid @RequestBody UserRegistrationRequestDTO request) {
        userService.updateUser(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable Long id) {
        userService.blockUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<Void> unblockUser(@PathVariable Long id) {
        userService.unblockUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable Long id,
                                                @Valid @RequestBody UserRoleUpdateRequestDTO request) {
        userService.updateUserRole(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id,
                                                @Valid @RequestBody ChangePasswordRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal) {
            Long currentUserId = ((UserPrincipal) auth.getPrincipal()).getId();
            if (!currentUserId.equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        userService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUserByAdmin(
            @Valid @RequestBody UserRegistrationRequestDTO request,
            @RequestParam String role) {
        UserResponseDTO user = userService.createUserByAdmin(request, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}