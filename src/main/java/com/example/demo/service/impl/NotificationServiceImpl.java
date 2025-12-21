// ПУТЬ: src/main/java/com/example/demo/service/impl/NotificationServiceImpl.java

package com.example.demo.service.impl;

import com.example.demo.entity.Notification;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public void sendReservationConfirmation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        Notification notification = Notification.builder()
                .user(reservation.getUser())
                .restaurant(reservation.getRestaurant())
                .reservation(reservation)
                .message("Ваше бронирование в ресторане " + reservation.getRestaurant().getName() +
                        " подтверждено на " + reservation.getReservationDate() + " в " + reservation.getStartTime())
                .type(Notification.NotificationType.CONFIRMATION)
                .isRead(false)
                .deliveryStatus(Notification.DeliveryStatus.SENT)
                .createdAt(LocalDateTime.now())
                .sentAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public void sendReservationReminder(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        Notification notification = Notification.builder()
                .user(reservation.getUser())
                .restaurant(reservation.getRestaurant())
                .reservation(reservation)
                .message("Напоминаем, что у вас бронирование в ресторане " +
                        reservation.getRestaurant().getName() + " сегодня в " + reservation.getStartTime())
                .type(Notification.NotificationType.REMINDER)
                .isRead(false)
                .deliveryStatus(Notification.DeliveryStatus.SENT)
                .createdAt(LocalDateTime.now())
                .sentAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public void sendCancellationNotification(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        Notification notification = Notification.builder()
                .user(reservation.getUser())
                .restaurant(reservation.getRestaurant())
                .reservation(reservation)
                .message("Ваше бронирование в ресторане " + reservation.getRestaurant().getName() +
                        " отменено. Причина: " + reservation.getCancelReason())
                .type(Notification.NotificationType.CANCELLATION)
                .isRead(false)
                .deliveryStatus(Notification.DeliveryStatus.SENT)
                .createdAt(LocalDateTime.now())
                .sentAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public void sendReviewRequest(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        Notification notification = Notification.builder()
                .user(reservation.getUser())
                .restaurant(reservation.getRestaurant())
                .reservation(reservation)
                .message("Спасибо за визит в " + reservation.getRestaurant().getName() +
                        "! Оставьте отзыв о вашем опыте")
                .type(Notification.NotificationType.REVIEW_REQUEST)
                .isRead(false)
                .deliveryStatus(Notification.DeliveryStatus.SENT)
                .createdAt(LocalDateTime.now())
                .sentAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Уведомление не найдено"));

        notification.setIsRead(true);
        notification.setDeliveryStatus(Notification.DeliveryStatus.READ);

        notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}