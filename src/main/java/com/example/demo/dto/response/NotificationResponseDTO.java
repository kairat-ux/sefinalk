package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private Long userId;
    private Long restaurantId;
    private Long reservationId;
    private String title;
    private String message;
    private String type;
    private Boolean isRead;
    private String deliveryStatus;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}
