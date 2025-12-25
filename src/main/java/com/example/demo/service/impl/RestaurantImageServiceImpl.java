package com.example.demo.service.impl;

import com.example.demo.entity.Restaurant;
import com.example.demo.entity.RestaurantImage;
import com.example.demo.repository.RestaurantImageRepository;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.service.RestaurantImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantImageServiceImpl implements RestaurantImageService {

    private final RestaurantImageRepository imageRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public RestaurantImage uploadImage(Long restaurantId, String imageUrl, String description) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        RestaurantImage image = RestaurantImage.builder()
                .restaurant(restaurant)
                .imageUrl(imageUrl)
                .description(description)
                .isMainImage(false)
                .build();

        return imageRepository.save(image);
    }

    @Override
    public RestaurantImage addImage(Long restaurantId, String imageUrl, String description, Boolean isMainImage) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (isMainImage != null && isMainImage) {
            List<RestaurantImage> allImages = imageRepository.findByRestaurantId(restaurantId);
            for (RestaurantImage img : allImages) {
                img.setIsMainImage(false);
            }
            imageRepository.saveAll(allImages);
        }

        RestaurantImage image = RestaurantImage.builder()
                .restaurant(restaurant)
                .imageUrl(imageUrl)
                .description(description)
                .isMainImage(isMainImage != null ? isMainImage : false)
                .build();

        return imageRepository.save(image);
    }

    @Override
    public List<RestaurantImage> getRestaurantImages(Long restaurantId) {
        return imageRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public RestaurantImage getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    @Override
    public void setMainImage(Long restaurantId, Long imageId) {
        List<RestaurantImage> allImages = imageRepository.findByRestaurantId(restaurantId);
        for (RestaurantImage img : allImages) {
            img.setIsMainImage(false);
        }
        imageRepository.saveAll(allImages);

        RestaurantImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        image.setIsMainImage(true);
        imageRepository.save(image);
    }

    @Override
    public void setPrimaryImage(Long restaurantId, Long imageId) {
        setMainImage(restaurantId, imageId);
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
}