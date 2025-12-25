package com.example.demo.controller;

import com.example.demo.dto.request.ReservationCreateRequestDTO;
import com.example.demo.dto.response.ReservationResponseDTO;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReservationController {

    private final ReservationService reservationService;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal) {
            return ((UserPrincipal) auth.getPrincipal()).getId();
        }
        throw new RuntimeException("User not authenticated");
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation(
            @Valid @RequestBody ReservationCreateRequestDTO request) {
        Long userId = getCurrentUserId();
        ReservationResponseDTO reservation = reservationService.createReservation(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        ReservationResponseDTO reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponseDTO>> getUserReservations(@PathVariable Long userId) {
        List<ReservationResponseDTO> reservations = reservationService.getUserReservations(userId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ReservationResponseDTO>> getRestaurantReservations(@PathVariable Long restaurantId) {
        List<ReservationResponseDTO> reservations = reservationService.getRestaurantReservations(restaurantId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/restaurant/{restaurantId}/date/{date}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByDate(
            @PathVariable Long restaurantId,
            @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<ReservationResponseDTO> reservations = reservationService.getReservationsByDate(restaurantId, localDate);
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReservation(@PathVariable Long id,
                                                  @Valid @RequestBody ReservationCreateRequestDTO request) {
        reservationService.updateReservation(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id,
                                                  @RequestParam(required = false) String reason) {
        reservationService.cancelReservation(id, reason != null ? reason : "Отмена пользователем");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{tableId}/available/{date}")
    public ResponseEntity<Boolean> isTableAvailable(@PathVariable Long tableId,
                                                    @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        boolean available = reservationService.isTableAvailable(tableId, localDate);
        return ResponseEntity.ok(available);
    }
}