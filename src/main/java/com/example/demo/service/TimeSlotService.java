package com.example.demo.service;

import com.example.demo.dto.request.TimeSlotCreateRequestDTO;
import com.example.demo.dto.response.TimeSlotResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface TimeSlotService {
    List<TimeSlotResponseDTO> getRestaurantTimeSlots(Long restaurantId, LocalDate date);
    TimeSlotResponseDTO getTimeSlotById(Long id);
    TimeSlotResponseDTO createTimeSlot(TimeSlotCreateRequestDTO request);
    void deleteTimeSlot(Long id);
    List<TimeSlotResponseDTO> getAvailableTimeSlots(Long restaurantId, LocalDate date);
    void blockTimeSlot(Long id, String reason);
    void unblockTimeSlot(Long id);
}
