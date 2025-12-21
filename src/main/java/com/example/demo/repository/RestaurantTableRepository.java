// ПУТЬ: src/main/java/com/example/demo/repository/RestaurantTableRepository.java

package com.example.demo.repository;

import com.example.demo.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    List<RestaurantTable> findByRestaurantId(Long restaurantId);
    List<RestaurantTable> findByRestaurantIdAndIsActiveTrue(Long restaurantId);
}