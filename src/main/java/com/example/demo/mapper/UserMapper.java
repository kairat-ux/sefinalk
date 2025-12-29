package com.example.demo.mapper;

import com.example.demo.dto.request.UserRegistrationRequestDTO;
import com.example.demo.dto.request.UserUpdateRequestDTO;
import com.example.demo.dto.response.UserProfileResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toUserResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .isActive(user.getIsActive())
                .isBlocked(user.getIsBlocked())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public UserProfileResponseDTO toUserProfileResponseDTO(User user, Integer totalReservations, Integer totalReviews) {
        if (user == null) {
            return null;
        }

        return UserProfileResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .isActive(user.getIsActive())
                .isBlocked(user.getIsBlocked())
                .createdAt(user.getCreatedAt())
                .totalReservations(totalReservations)
                .totalReviews(totalReviews)
                .build();
    }

    public User toEntity(UserRegistrationRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .role(User.UserRole.USER)
                .isActive(true)
                .isBlocked(false)
                .build();
    }

    public void updateEntityFromDTO(UserUpdateRequestDTO dto, User user) {
        if (dto == null || user == null) {
            return;
        }

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
    }
}
