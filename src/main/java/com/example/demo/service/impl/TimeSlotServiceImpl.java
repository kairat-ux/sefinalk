// ПУТЬ: src/main/java/com/example/demo/service/impl/TimeSlotServiceImpl.java

package com.example.demo.service.impl;

import com.example.demo.dto.request.TimeSlotCreateRequestDTO;
import com.example.demo.dto.response.TimeSlotResponseDTO;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.RestaurantTable;
import com.example.demo.entity.TimeSlot;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.repository.RestaurantTableRepository;
import com.example.demo.repository.TimeSlotRepository;
import com.example.demo.service.TimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TimeSlotServiceImpl implements TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TimeSlotResponseDTO> getRestaurantTimeSlots(Long restaurantId, LocalDate date) {
        List<TimeSlot> timeSlots;

        if (date != null) {
            timeSlots = timeSlotRepository.findByRestaurantIdAndSlotDate(restaurantId, date);
        } else {
            timeSlots = timeSlotRepository.findAvailableSlots(restaurantId, LocalDate.now());
        }

        return timeSlots.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TimeSlotResponseDTO getTimeSlotById(Long id) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Таймслот не найден"));
        return mapToDTO(timeSlot);
    }

    @Override
    public TimeSlotResponseDTO createTimeSlot(TimeSlotCreateRequestDTO request) {
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Ресторан не найден"));

        RestaurantTable table = restaurantTableRepository.findById(request.getTableId())
                .orElseThrow(() -> new RuntimeException("Столик не найден"));

        // Проверка что столик принадлежит ресторану
        if (!table.getRestaurant().getId().equals(restaurant.getId())) {
            throw new RuntimeException("Столик не принадлежит данному ресторану");
        }

        TimeSlot timeSlot = TimeSlot.builder()
                .restaurant(restaurant)
                .table(table)
                .slotDate(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .availableSeats(table.getCapacity())
                .isBlocked(request.getIsBlocked() != null ? request.getIsBlocked() : false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TimeSlot savedTimeSlot = timeSlotRepository.save(timeSlot);
        return mapToDTO(savedTimeSlot);
    }

    @Override
    public void deleteTimeSlot(Long id) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Таймслот не найден"));

        timeSlotRepository.delete(timeSlot);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeSlotResponseDTO> getAvailableTimeSlots(Long restaurantId, LocalDate date) {
        List<TimeSlot> timeSlots = timeSlotRepository.findByRestaurantIdAndSlotDate(restaurantId, date);

        return timeSlots.stream()
                .filter(ts -> !ts.getIsBlocked() && ts.getAvailableSeats() > 0)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void blockTimeSlot(Long id, String reason) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Таймслот не найден"));

        timeSlot.setIsBlocked(true);
        timeSlot.setBlockReason(reason);
        timeSlot.setUpdatedAt(LocalDateTime.now());

        timeSlotRepository.save(timeSlot);
    }

    @Override
    public void unblockTimeSlot(Long id) {
        TimeSlot timeSlot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Таймслот не найден"));

        timeSlot.setIsBlocked(false);
        timeSlot.setBlockReason(null);
        timeSlot.setUpdatedAt(LocalDateTime.now());

        timeSlotRepository.save(timeSlot);
    }

    private TimeSlotResponseDTO mapToDTO(TimeSlot timeSlot) {
        return TimeSlotResponseDTO.builder()
                .id(timeSlot.getId())
                .restaurantId(timeSlot.getRestaurant().getId())
                .tableId(timeSlot.getTable().getId())
                .tableName("Table " + timeSlot.getTable().getTableNumber())
                .date(timeSlot.getSlotDate())
                .startTime(timeSlot.getStartTime())
                .endTime(timeSlot.getEndTime())
                .isAvailable(timeSlot.getAvailableSeats() > 0)
                .isBlocked(timeSlot.getIsBlocked())
                .reservationId(null) // TODO: добавить связь с резервацией при необходимости
                .createdAt(timeSlot.getCreatedAt())
                .build();
    }
}
