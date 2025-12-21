// ПУТЬ: src/main/java/com/example/demo/service/UserService.java

package com.example.demo.service;

import com.example.demo.dto.request.UserRegistrationRequestDTO;
import com.example.demo.dto.response.UserResponseDTO;
import java.util.List;

public interface UserService {
    UserResponseDTO register(UserRegistrationRequestDTO request);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByEmail(String email);
    List<UserResponseDTO> getAllUsers();
    void updateUser(Long id, UserRegistrationRequestDTO request);
    void deleteUser(Long id);
    void blockUser(Long id);
    void unblockUser(Long id);
}