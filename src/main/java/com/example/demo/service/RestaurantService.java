package com.example.demo.service;

import com.example.demo.dto.request.RestaurantCreateRequestDTO;
import com.example.demo.dto.response.RestaurantDetailResponseDTO;
import java.util.List;

public interface RestaurantService {
    RestaurantDetailResponseDTO createRestaurant(RestaurantCreateRequestDTO request, Long ownerId);
    RestaurantDetailResponseDTO getRestaurantById(Long id);
    List<RestaurantDetailResponseDTO> getAllRestaurants();
    List<RestaurantDetailResponseDTO> getRestaurantsByCity(String city);
    List<RestaurantDetailResponseDTO> getOwnerRestaurants(Long ownerId);
    void updateRestaurant(Long id, RestaurantCreateRequestDTO request);
    void deleteRestaurant(Long id);
}