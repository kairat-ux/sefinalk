package com.example.demo.service;

import com.example.demo.entity.RestaurantImage;
import java.util.List;

public interface RestaurantImageService {
    RestaurantImage uploadImage(Long restaurantId, String imageUrl, String description);
    RestaurantImage addImage(Long restaurantId, String imageUrl, String description, Boolean isMainImage);
    List<RestaurantImage> getRestaurantImages(Long restaurantId);
    RestaurantImage getImageById(Long id);
    void setMainImage(Long restaurantId, Long imageId);
    void setPrimaryImage(Long restaurantId, Long imageId);
    void deleteImage(Long id);
}