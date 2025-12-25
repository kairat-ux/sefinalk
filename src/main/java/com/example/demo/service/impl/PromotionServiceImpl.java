package com.example.demo.service.impl;

import com.example.demo.entity.Promotion;
import com.example.demo.entity.Restaurant;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Promotion> getActivePromotions() {
        return promotionRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Promotion> getPromotionsByRestaurantId(Long restaurantId) {
        return promotionRepository.findByRestaurantId(restaurantId);
    }

    @Override
    @Transactional(readOnly = true)
    public Promotion getPromotionById(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + id));
    }

    @Override
    public Promotion createPromotion(Long restaurantId, String title, String description,
                                     Integer discountPercentage, LocalDate startDate, LocalDate endDate) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));

        if (endDate.isBefore(startDate)) {
            throw new RuntimeException("End date cannot be before start date");
        }

        Promotion promotion = Promotion.builder()
                .restaurant(restaurant)
                .title(title)
                .description(description)
                .discountPercentage(discountPercentage)
                .startDate(startDate)
                .endDate(endDate)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        return promotionRepository.save(promotion);
    }

    @Override
    public Promotion updatePromotion(Long id, String title, String description,
                                     Integer discountPercentage, LocalDate startDate, LocalDate endDate) {
        Promotion promotion = getPromotionById(id);

        if (title != null) {
            promotion.setTitle(title);
        }
        if (description != null) {
            promotion.setDescription(description);
        }
        if (discountPercentage != null) {
            promotion.setDiscountPercentage(discountPercentage);
        }
        if (startDate != null) {
            promotion.setStartDate(startDate);
        }
        if (endDate != null) {
            if (startDate != null && endDate.isBefore(startDate)) {
                throw new RuntimeException("End date cannot be before start date");
            }
            promotion.setEndDate(endDate);
        }

        return promotionRepository.save(promotion);
    }

    @Override
    public void deletePromotion(Long id) {
        Promotion promotion = getPromotionById(id);
        promotionRepository.delete(promotion);
    }

    @Override
    public void activatePromotion(Long id) {
        Promotion promotion = getPromotionById(id);
        promotion.setIsActive(true);
        promotionRepository.save(promotion);
    }

    @Override
    public void deactivatePromotion(Long id) {
        Promotion promotion = getPromotionById(id);
        promotion.setIsActive(false);
        promotionRepository.save(promotion);
    }
}
