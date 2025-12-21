// ПУТЬ: src/main/java/com/example/demo/repository/RestaurantImageRepository.java

package com.example.demo.repository;

import com.example.demo.entity.RestaurantImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantImageRepository extends JpaRepository<RestaurantImage, Long> {
    List<RestaurantImage> findByRestaurantId(Long restaurantId);
}