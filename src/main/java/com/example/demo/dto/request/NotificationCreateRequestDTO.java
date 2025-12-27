package com.example.demo.dto.request;

import com.example.demo.entity.Notification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateRequestDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    private Long restaurantId;

    private Long reservationId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Notification type is required")
    private Notification.NotificationType type;
}
