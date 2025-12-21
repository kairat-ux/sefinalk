// ПУТЬ: src/main/java/com/example/demo/repository/ReservationRepository.java

package com.example.demo.repository;

import com.example.demo.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByRestaurantId(Long restaurantId);
    List<Reservation> findByTableId(Long tableId);

    @Query("SELECT r FROM Reservation r WHERE r.restaurant.id = :restaurantId AND r.reservationDate = :date")
    List<Reservation> findByRestaurantAndDate(Long restaurantId, LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId ORDER BY r.createdAt DESC")
    List<Reservation> findUserReservations(Long userId);
}