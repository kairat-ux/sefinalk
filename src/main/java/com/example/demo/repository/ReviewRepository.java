// ПУТЬ: src/main/java/com/example/demo/repository/ReviewRepository.java

package com.example.demo.repository;

import com.example.demo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRestaurantId(Long restaurantId);
    List<Review> findByRestaurantIdAndIsApprovedTrue(Long restaurantId);
    List<Review> findByUserId(Long userId);
    List<Review> findByIsApprovedFalse();
}