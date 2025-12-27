// ПУТЬ: src/main/java/com/example/demo/service/ReservationService.java

package com.example.demo.service;

import com.example.demo.dto.request.ReservationCreateRequestDTO;
import com.example.demo.dto.response.ReservationResponseDTO;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    ReservationResponseDTO createReservation(ReservationCreateRequestDTO request, Long userId);
    ReservationResponseDTO getReservationById(Long id);
    List<ReservationResponseDTO> getUserReservations(Long userId);
    List<ReservationResponseDTO> getRestaurantReservations(Long restaurantId);
    List<ReservationResponseDTO> getReservationsByDate(Long restaurantId, LocalDate date);
    void updateReservation(Long id, ReservationCreateRequestDTO request);
    void cancelReservation(Long id, String reason);
}