package com.example.demo.mapper;

import com.example.demo.dto.request.TimeSlotCreateRequestDTO;
import com.example.demo.dto.response.TimeSlotResponseDTO;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.TimeSlot;
import org.springframework.stereotype.Component;

@Component
public class TimeSlotMapper {

    public TimeSlotResponseDTO toResponseDTO(TimeSlot timeSlot) {
        if (timeSlot == null) {
            return null;
        }

        return TimeSlotResponseDTO.builder()
                .id(timeSlot.getId())
                .restaurantId(timeSlot.getRestaurant() != null ? timeSlot.getRestaurant().getId() : null)
                .date(timeSlot.getSlotDate())
                .startTime(timeSlot.getStartTime())
                .endTime(timeSlot.getEndTime())
                .availableSeats(timeSlot.getAvailableSeats())
                .isBlocked(timeSlot.getIsBlocked())
                .createdAt(timeSlot.getCreatedAt())
                .build();
    }

    public TimeSlot toEntity(TimeSlotCreateRequestDTO dto, Restaurant restaurant) {
        if (dto == null || restaurant == null) {
            return null;
        }

        return TimeSlot.builder()
                .restaurant(restaurant)
                .slotDate(dto.getDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .availableSeats(dto.getAvailableSeats())
                .isBlocked(dto.getIsBlocked() != null ? dto.getIsBlocked() : false)
                .build();
    }
}
