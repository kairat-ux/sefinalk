// ПУТЬ: src/main/java/com/example/demo/controller/NotificationController.java

package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send-confirmation/{reservationId}")
    public ResponseEntity<Void> sendConfirmation(@PathVariable Long reservationId) {
        notificationService.sendReservationConfirmation(reservationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send-reminder/{reservationId}")
    public ResponseEntity<Void> sendReminder(@PathVariable Long reservationId) {
        notificationService.sendReservationReminder(reservationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send-cancellation/{reservationId}")
    public ResponseEntity<Void> sendCancellation(@PathVariable Long reservationId) {
        notificationService.sendCancellationNotification(reservationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send-review-request/{reservationId}")
    public ResponseEntity<Void> sendReviewRequest(@PathVariable Long reservationId) {
        notificationService.sendReviewRequest(reservationId);
        return ResponseEntity.noContent().build();
    }
}