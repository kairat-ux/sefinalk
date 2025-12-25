package com.example.demo.service.impl;

import com.example.demo.entity.Promotion;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    public Promotion createPromotion(String code, BigDecimal discount, Promotion.DiscountType discountType,
                                     LocalDate validFrom, LocalDate validTo, Integer maxUsage) {
        Promotion promotion = Promotion.builder()
                .code(code)
                .discount(discount)
                .discountType(discountType)
                .validFrom(validFrom)
                .validTo(validTo)
                .maxUsage(maxUsage)
                .build();

        return promotionRepository.save(promotion);
    }

    public Promotion getPromotionByCode(String code) {
        return promotionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
    }

    public Promotion getPromotionById(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
    }

    public List<Promotion> getRestaurantPromotions(Long restaurantId) {
        return promotionRepository.findByRestaurantIdAndIsActiveTrue(restaurantId);
    }

    public void updatePromotion(Long id, String code, BigDecimal discount, Promotion.DiscountType discountType,
                                LocalDate validFrom, LocalDate validTo, Integer maxUsage) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));

        if (code != null && !code.equals(promotion.getCode())) {
            promotion.setCode(code);
        }

        if (discount != null) {
            promotion.setDiscount(discount);
        }

        if (discountType != null) {
            promotion.setDiscountType(discountType);
        }

        if (validFrom != null) {
            promotion.setValidFrom(validFrom);
        }

        if (validTo != null) {
            promotion.setValidTo(validTo);
        }

        if (maxUsage != null) {
            promotion.setMaxUsage(maxUsage);
        }

        promotionRepository.save(promotion);
    }

    public void deletePromotion(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        promotion.setIsActive(false);
        promotionRepository.save(promotion);
    }
}