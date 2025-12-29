package com.example.demo.mapper;

import com.example.demo.dto.request.ReservationCreateRequestDTO;
import com.example.demo.dto.request.ReservationUpdateRequestDTO;
import com.example.demo.dto.response.ReservationResponseDTO;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationMapper {

    public ReservationResponseDTO toResponseDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        return ReservationResponseDTO.builder()
                .id(reservation.getId())
                .userId(reservation.getUser() != null ? reservation.getUser().getId() : null)
                .restaurantId(reservation.getRestaurant() != null ? reservation.getRestaurant().getId() : null)
                .reservationDate(reservation.getReservationDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .guestCount(reservation.getGuestCount())
                .status(reservation.getStatus() != null ? reservation.getStatus().name() : null)
                .specialRequests(reservation.getSpecialRequests())
                .createdAt(reservation.getCreatedAt())
                .restaurantName(reservation.getRestaurant() != null ? reservation.getRestaurant().getName() : null)
                .restaurantCity(reservation.getRestaurant() != null ? reservation.getRestaurant().getCity() : null)
                .numberOfGuests(reservation.getGuestCount())
                .reservationTime(LocalDateTime.of(reservation.getReservationDate(), reservation.getStartTime()))
                .build();
    }

    public Reservation toEntity(ReservationCreateRequestDTO dto, User user, Restaurant restaurant) {
        if (dto == null || user == null || restaurant == null) {
            return null;
        }

        return Reservation.builder()
                .user(user)
                .restaurant(restaurant)
                .reservationDate(dto.getReservationDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .guestCount(dto.getGuestCount())
                .specialRequests(dto.getSpecialRequests())
                .status(Reservation.ReservationStatus.PENDING)
                .build();
    }

    public void updateEntityFromDTO(ReservationUpdateRequestDTO dto, Reservation reservation) {
        if (dto == null || reservation == null) {
            return;
        }

        if (dto.getReservationDate() != null) {
            reservation.setReservationDate(dto.getReservationDate());
        }
        if (dto.getStartTime() != null) {
            reservation.setStartTime(dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            reservation.setEndTime(dto.getEndTime());
        }
        if (dto.getGuestCount() != null) {
            reservation.setGuestCount(dto.getGuestCount());
        }
        if (dto.getSpecialRequests() != null) {
            reservation.setSpecialRequests(dto.getSpecialRequests());
        }
    }
}
