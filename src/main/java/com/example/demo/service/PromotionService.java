package com.example.demo.service;

import com.example.demo.entity.Promotion;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PromotionService {
    Promotion createPromotion(String code, BigDecimal discount, Promotion.DiscountType discountType,
                              LocalDate validFrom, LocalDate validTo, Integer maxUsage);
    Promotion getPromotionByCode(String code);
    Promotion getPromotionById(Long id);
    List<Promotion> getRestaurantPromotions(Long restaurantId);
    void updatePromotion(Long id, String code, BigDecimal discount, Promotion.DiscountType discountType,
                         LocalDate validFrom, LocalDate validTo, Integer maxUsage);
    void deletePromotion(Long id);
}