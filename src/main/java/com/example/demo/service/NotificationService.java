// ПУТЬ: src/main/java/com/example/demo/service/NotificationService.java

package com.example.demo.service;

import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.entity.Notification;
import java.util.List;

public interface NotificationService {
    void sendReservationConfirmation(Long reservationId);
    void sendReservationReminder(Long reservationId);
    void sendCancellationNotification(Long reservationId);
    void sendReviewRequest(Long reservationId);
    List<Notification> getUserNotifications(Long userId);
    List<Notification> getUnreadNotifications(Long userId);
    void markAsRead(Long notificationId);
    void deleteNotification(Long notificationId);
}