package com.example.demo.mapper;

import com.example.demo.dto.request.RestaurantCreateRequestDTO;
import com.example.demo.dto.request.RestaurantUpdateRequestDTO;
import com.example.demo.dto.response.RestaurantDetailResponseDTO;
import com.example.demo.dto.response.RestaurantListResponseDTO;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    public RestaurantDetailResponseDTO toDetailResponseDTO(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        return RestaurantDetailResponseDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .address(restaurant.getAddress())
                .city(restaurant.getCity())
                .phone(restaurant.getPhone())
                .email(restaurant.getEmail())
                .rating(restaurant.getRating())
                .totalReviews(restaurant.getTotalReviews())
                .createdAt(restaurant.getCreatedAt())
                .build();
    }

    public RestaurantListResponseDTO toListResponseDTO(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        return RestaurantListResponseDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .address(restaurant.getAddress())
                .city(restaurant.getCity())
                .phone(restaurant.getPhone())
                .email(restaurant.getEmail())
                .rating(restaurant.getRating())
                .totalReviews(restaurant.getTotalReviews())
                .build();
    }

    public Restaurant toEntity(RestaurantCreateRequestDTO dto, User owner) {
        if (dto == null || owner == null) {
            return null;
        }

        return Restaurant.builder()
                .owner(owner)
                .name(dto.getName())
                .description(dto.getDescription())
                .address(dto.getAddress())
                .city(dto.getCity())
                .zipCode(dto.getZipCode())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .isActive(true)
                .build();
    }

    public void updateEntityFromDTO(RestaurantUpdateRequestDTO dto, Restaurant restaurant) {
        if (dto == null || restaurant == null) {
            return;
        }

        if (dto.getName() != null) {
            restaurant.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            restaurant.setDescription(dto.getDescription());
        }
        if (dto.getAddress() != null) {
            restaurant.setAddress(dto.getAddress());
        }
        if (dto.getCity() != null) {
            restaurant.setCity(dto.getCity());
        }
        if (dto.getZipCode() != null) {
            restaurant.setZipCode(dto.getZipCode());
        }
        if (dto.getPhone() != null) {
            restaurant.setPhone(dto.getPhone());
        }
        if (dto.getEmail() != null) {
            restaurant.setEmail(dto.getEmail());
        }
    }
}
