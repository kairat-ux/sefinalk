// ПУТЬ: src/main/java/com/example/demo/dto/response/ReservationResponseDTO.java

package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {
    private Long id;
    private Long userId;
    private Long restaurantId;
    private Long tableId;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer guestCount;
    private String status;
    private String specialRequests;
    private LocalDateTime createdAt;

    // Additional fields for frontend compatibility
    private String restaurantName;
    private String restaurantCity;
    private Integer numberOfGuests;
    private LocalDateTime reservationTime;
}