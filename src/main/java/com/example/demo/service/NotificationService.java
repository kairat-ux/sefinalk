package com.example.demo.service;

import com.example.demo.dto.request.NotificationCreateRequestDTO;
import com.example.demo.dto.response.NotificationResponseDTO;
import java.util.List;

public interface NotificationService {
    NotificationResponseDTO createNotification(NotificationCreateRequestDTO request);
    NotificationResponseDTO getNotificationById(Long id);
    List<NotificationResponseDTO> getUserNotifications(Long userId);
    List<NotificationResponseDTO> getUnreadNotifications(Long userId);
    void markAsRead(Long id);
    void markAllAsRead(Long userId);
    void deleteNotification(Long id);
}
