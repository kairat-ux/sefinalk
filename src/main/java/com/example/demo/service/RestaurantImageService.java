package com.example.demo.service;

import com.example.demo.entity.RestaurantImage;

import java.util.List;

public interface RestaurantImageService {
    List<RestaurantImage> getImagesByRestaurantId(Long restaurantId);
    RestaurantImage getImageById(Long id);
    RestaurantImage addImage(Long restaurantId, String imageUrl, Boolean isPrimary);
    void deleteImage(Long id);
    void setPrimaryImage(Long imageId);
}
