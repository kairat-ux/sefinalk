package com.example.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotCreateRequestDTO {

    @NotNull(message = "ID ресторана обязателен")
    private Long restaurantId;

    @NotNull(message = "Дата обязательна")
    private LocalDate date;

    @NotNull(message = "Время начала обязательно")
    private LocalTime startTime;

    @NotNull(message = "Время окончания обязательно")
    private LocalTime endTime;

    @NotNull(message = "Количество доступных мест обязательно")
    private Integer availableSeats;

    private Boolean isBlocked;
}
