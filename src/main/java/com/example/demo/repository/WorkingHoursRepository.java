// ПУТЬ: src/main/java/com/example/demo/repository/WorkingHoursRepository.java

package com.example.demo.repository;

import com.example.demo.entity.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {
    Optional<WorkingHours> findByRestaurantIdAndDayOfWeek(Long restaurantId, Integer dayOfWeek);
}