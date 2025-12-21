// ПУТЬ: src/main/java/com/example/demo/dto/request/ReservationCreateRequestDTO.java

package com.example.demo.dto.request;

import jakarta.validation.constraints.*;
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
public class ReservationCreateRequestDTO {

    @NotNull(message = "ID ресторана обязателен")
    private Long restaurantId;

    @NotNull(message = "ID столика обязателен")
    private Long tableId;

    @NotNull(message = "Дата обязательна")
    @Future(message = "Дата должна быть в будущем")
    private LocalDate reservationDate;

    @NotNull(message = "Время начала обязательно")
    private LocalTime startTime;

    @NotNull(message = "Время окончания обязательно")
    private LocalTime endTime;

    @NotNull(message = "Количество гостей обязательно")
    @Min(value = 1, message = "Минимум 1 гость")
    private Integer guestCount;

    private String specialRequests;
}