package com.example.demo.service.impl;

import com.example.demo.entity.Restaurant;
import com.example.demo.entity.RestaurantImage;
import com.example.demo.repository.RestaurantImageRepository;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.service.RestaurantImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantImageServiceImpl implements RestaurantImageService {

    private final RestaurantImageRepository restaurantImageRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantImage> getImagesByRestaurantId(Long restaurantId) {
        return restaurantImageRepository.findByRestaurantId(restaurantId);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantImage getImageById(Long id) {
        return restaurantImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + id));
    }

    @Override
    public RestaurantImage addImage(Long restaurantId, String imageUrl, Boolean isPrimary) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));

        if (Boolean.TRUE.equals(isPrimary)) {
            List<RestaurantImage> existingImages = restaurantImageRepository.findByRestaurantId(restaurantId);
            existingImages.forEach(img -> {
                img.setIsPrimary(false);
                restaurantImageRepository.save(img);
            });
        }

        RestaurantImage image = RestaurantImage.builder()
                .restaurant(restaurant)
                .imageUrl(imageUrl)
                .isPrimary(isPrimary != null ? isPrimary : false)
                .uploadedAt(LocalDateTime.now())
                .build();

        return restaurantImageRepository.save(image);
    }

    @Override
    public void deleteImage(Long id) {
        RestaurantImage image = getImageById(id);
        restaurantImageRepository.delete(image);
    }

    @Override
    public void setPrimaryImage(Long imageId) {
        RestaurantImage image = getImageById(imageId);
        
        List<RestaurantImage> existingImages = restaurantImageRepository
                .findByRestaurantId(image.getRestaurant().getId());
        
        existingImages.forEach(img -> {
            img.setIsPrimary(false);
            restaurantImageRepository.save(img);
        });

        image.setIsPrimary(true);
        restaurantImageRepository.save(image);
    }
}
