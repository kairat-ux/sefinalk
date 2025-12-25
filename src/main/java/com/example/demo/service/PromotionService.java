package com.example.demo.service;

import com.example.demo.entity.Promotion;

import java.time.LocalDate;
import java.util.List;

public interface PromotionService {
    List<Promotion> getAllPromotions();
    List<Promotion> getActivePromotions();
    List<Promotion> getPromotionsByRestaurantId(Long restaurantId);
    Promotion getPromotionById(Long id);
    Promotion createPromotion(Long restaurantId, String title, String description,
                              Integer discountPercentage, LocalDate startDate, LocalDate endDate);
    Promotion updatePromotion(Long id, String title, String description,
                              Integer discountPercentage, LocalDate startDate, LocalDate endDate);
    void deletePromotion(Long id);
    void activatePromotion(Long id);
    void deactivatePromotion(Long id);
}
