package com.example.demo.service.impl;

import com.example.demo.dto.request.ReservationCreateRequestDTO;
import com.example.demo.dto.response.ReservationResponseDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public ReservationResponseDTO createReservation(ReservationCreateRequestDTO request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Ресторан не найден"));

        Reservation reservation = Reservation.builder()
                .user(user)
                .restaurant(restaurant)
                .reservationDate(request.getReservationDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .guestCount(request.getGuestCount())
                .specialRequests(request.getSpecialRequests())
                .status(Reservation.ReservationStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        // Создаем нотификацию о подтверждении резервации
        createConfirmationNotification(savedReservation);

        return mapToDTO(savedReservation);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));
        return mapToDTO(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getUserReservations(Long userId) {
        return reservationRepository.findUserReservations(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getRestaurantReservations(Long restaurantId) {
        return reservationRepository.findByRestaurantId(restaurantId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getReservationsByDate(Long restaurantId, LocalDate date) {
        return reservationRepository.findByRestaurantAndDate(restaurantId, date).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateReservation(Long id, ReservationCreateRequestDTO request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        reservation.setReservationDate(request.getReservationDate());
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setGuestCount(request.getGuestCount());
        reservation.setSpecialRequests(request.getSpecialRequests());
        reservation.setUpdatedAt(LocalDateTime.now());

        reservationRepository.save(reservation);
    }

    @Override
    public void cancelReservation(Long id, String reason) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());
        reservation.setCancelReason(reason);
        reservation.setUpdatedAt(LocalDateTime.now());

        reservationRepository.save(reservation);

        // Создаем нотификацию об отмене резервации
        createCancellationNotification(reservation);
    }

    private ReservationResponseDTO mapToDTO(Reservation reservation) {
        return ReservationResponseDTO.builder()
                .id(reservation.getId())
                .userId(reservation.getUser().getId())
                .restaurantId(reservation.getRestaurant().getId())
                .reservationDate(reservation.getReservationDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .guestCount(reservation.getGuestCount())
                .status(reservation.getStatus().toString())
                .specialRequests(reservation.getSpecialRequests())
                .createdAt(reservation.getCreatedAt())
                .build();
    }

    private void createConfirmationNotification(Reservation reservation) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String formattedDate = reservation.getReservationDate().format(dateFormatter);
            String formattedTime = reservation.getStartTime().format(timeFormatter);

            Notification notification = Notification.builder()
                    .user(reservation.getUser())
                    .restaurant(reservation.getRestaurant())
                    .reservation(reservation)
                    .title("Reservation Confirmed")
                    .message(String.format(
                            "Your reservation for %d guest%s at %s has been confirmed for %s at %s",
                            reservation.getGuestCount(),
                            reservation.getGuestCount() > 1 ? "s" : "",
                            reservation.getRestaurant().getName(),
                            formattedDate,
                            formattedTime
                    ))
                    .type(Notification.NotificationType.CONFIRMATION)
                    .isRead(false)
                    .deliveryStatus(Notification.DeliveryStatus.SENT)
                    .createdAt(LocalDateTime.now())
                    .sentAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
        } catch (Exception e) {
            // Не прерываем процесс резервации если нотификация не отправилась
            System.err.println("Failed to create confirmation notification: " + e.getMessage());
        }
    }

    private void createCancellationNotification(Reservation reservation) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String formattedDate = reservation.getReservationDate().format(dateFormatter);
            String formattedTime = reservation.getStartTime().format(timeFormatter);

            String message = String.format(
                    "Your reservation at %s for %s at %s has been cancelled.",
                    reservation.getRestaurant().getName(),
                    formattedDate,
                    formattedTime
            );

            if (reservation.getCancelReason() != null && !reservation.getCancelReason().isEmpty()) {
                message += " Reason: " + reservation.getCancelReason();
            }

            Notification notification = Notification.builder()
                    .user(reservation.getUser())
                    .restaurant(reservation.getRestaurant())
                    .reservation(reservation)
                    .title("Reservation Cancelled")
                    .message(message)
                    .type(Notification.NotificationType.CANCELLATION)
                    .isRead(false)
                    .deliveryStatus(Notification.DeliveryStatus.SENT)
                    .createdAt(LocalDateTime.now())
                    .sentAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
        } catch (Exception e) {
            System.err.println("Failed to create cancellation notification: " + e.getMessage());
        }
    }
}