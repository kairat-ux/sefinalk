// ПУТЬ: src/main/java/com/example/demo/repository/TimeSlotRepository.java

package com.example.demo.repository;

import com.example.demo.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByRestaurantIdAndSlotDate(Long restaurantId, LocalDate slotDate);

    @Query("SELECT t FROM TimeSlot t WHERE t.restaurant.id = :restaurantId AND t.slotDate >= :date ORDER BY t.slotDate")
    List<TimeSlot> findAvailableSlots(Long restaurantId, LocalDate date);
}