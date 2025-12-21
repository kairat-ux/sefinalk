// ПУТЬ: src/main/java/com/example/demo/service/impl/RestaurantServiceImpl.java

package com.example.demo.service.impl;

import com.example.demo.dto.request.RestaurantCreateRequestDTO;
import com.example.demo.dto.response.RestaurantDetailResponseDTO;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.User;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Override
    public RestaurantDetailResponseDTO createRestaurant(RestaurantCreateRequestDTO request, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Владелец не найден"));

        Restaurant restaurant = Restaurant.builder()
                .owner(owner)
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .city(request.getCity())
                .zipCode(request.getZipCode())
                .phone(request.getPhone())
                .email(request.getEmail())
                .rating(BigDecimal.ZERO)
                .totalReviews(0)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return mapToDTO(savedRestaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantDetailResponseDTO getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ресторан не найден"));
        return mapToDTO(restaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantDetailResponseDTO> getAllRestaurants() {
        return restaurantRepository.findAllActive().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantDetailResponseDTO> getRestaurantsByCity(String city) {
        return restaurantRepository.findByCity(city).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantDetailResponseDTO> getOwnerRestaurants(Long ownerId) {
        return restaurantRepository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateRestaurant(Long id, RestaurantCreateRequestDTO request) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ресторан не найден"));

        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setAddress(request.getAddress());
        restaurant.setCity(request.getCity());
        restaurant.setZipCode(request.getZipCode());
        restaurant.setPhone(request.getPhone());
        restaurant.setEmail(request.getEmail());
        restaurant.setUpdatedAt(LocalDateTime.now());

        restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ресторан не найден"));
        restaurant.setIsActive(false);
        restaurant.setUpdatedAt(LocalDateTime.now());
        restaurantRepository.save(restaurant);
    }

    private RestaurantDetailResponseDTO mapToDTO(Restaurant restaurant) {
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
                .isActive(restaurant.getIsActive())
                .createdAt(restaurant.getCreatedAt())
                .build();
    }
}