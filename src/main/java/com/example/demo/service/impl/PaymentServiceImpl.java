// ПУТЬ: src/main/java/com/example/demo/service/impl/PaymentServiceImpl.java

package com.example.demo.service.impl;

import com.example.demo.dto.request.PaymentCreateRequestDTO;
import com.example.demo.dto.response.PaymentResponseDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public PaymentResponseDTO createPayment(PaymentCreateRequestDTO request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        Payment payment = Payment.builder()
                .reservation(reservation)
                .user(user)
                .amount(request.getAmount())
                .currency("KZT")
                .paymentMethod(Payment.PaymentMethod.valueOf(request.getPaymentMethod()))
                .status(Payment.PaymentStatus.PENDING)
                .transactionId("TXN-" + System.currentTimeMillis())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        return mapToDTO(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Платёж не найден"));
        return mapToDTO(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentByReservationId(Long reservationId) {
        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new RuntimeException("Платёж не найден"));
        return mapToDTO(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDTO> getUserPayments(Long userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void completePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Платёж не найден"));

        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment.setProcessedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        // Создаем нотификацию об успешной оплате
        createPaymentCompletedNotification(payment);
    }

    @Override
    public void refundPayment(Long id, String reason) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Платёж не найден"));

        if (payment.getStatus() != Payment.PaymentStatus.COMPLETED) {
            throw new RuntimeException("Возврат возможен только для завершённых платежей");
        }

        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        payment.setRefundedAt(LocalDateTime.now());
        payment.setRefundReason(reason);
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        // Создаем нотификацию о возврате средств
        createRefundNotification(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateRefundAmount(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new RuntimeException("Платёж не найден"));

        long hoursUntilReservation = ChronoUnit.HOURS.between(
                LocalDateTime.now(),
                reservation.getReservationDate().atTime(reservation.getStartTime())
        );

        if (hoursUntilReservation >= 24) {
            return payment.getAmount(); // 100% возврат
        } else if (hoursUntilReservation >= 2) {
            return payment.getAmount().multiply(BigDecimal.valueOf(0.5)); // 50% возврат
        } else {
            return BigDecimal.ZERO; // 0% возврат
        }
    }

    private PaymentResponseDTO mapToDTO(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .reservationId(payment.getReservation().getId())
                .userId(payment.getUser().getId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod().toString())
                .status(payment.getStatus().toString())
                .transactionId(payment.getTransactionId())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    private void createPaymentCompletedNotification(Payment payment) {
        try {
            Reservation reservation = payment.getReservation();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            String formattedDate = reservation.getReservationDate().format(dateFormatter);

            Notification notification = Notification.builder()
                    .user(payment.getUser())
                    .restaurant(reservation.getRestaurant())
                    .reservation(reservation)
                    .title("Payment Successful")
                    .message(String.format(
                            "Your payment of %s KZT for reservation at %s on %s has been processed successfully.",
                            payment.getAmount(),
                            reservation.getRestaurant().getName(),
                            formattedDate
                    ))
                    .type(Notification.NotificationType.CONFIRMATION)
                    .isRead(false)
                    .deliveryStatus(Notification.DeliveryStatus.SENT)
                    .createdAt(LocalDateTime.now())
                    .sentAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
        } catch (Exception e) {
            System.err.println("Failed to create payment completed notification: " + e.getMessage());
        }
    }

    private void createRefundNotification(Payment payment) {
        try {
            Reservation reservation = payment.getReservation();

            String message = String.format(
                    "Your payment of %s KZT has been refunded.",
                    payment.getAmount()
            );

            if (payment.getRefundReason() != null && !payment.getRefundReason().isEmpty()) {
                message += " Reason: " + payment.getRefundReason();
            }

            Notification notification = Notification.builder()
                    .user(payment.getUser())
                    .restaurant(reservation.getRestaurant())
                    .reservation(reservation)
                    .title("Payment Refunded")
                    .message(message)
                    .type(Notification.NotificationType.CONFIRMATION)
                    .isRead(false)
                    .deliveryStatus(Notification.DeliveryStatus.SENT)
                    .createdAt(LocalDateTime.now())
                    .sentAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
        } catch (Exception e) {
            System.err.println("Failed to create refund notification: " + e.getMessage());
        }
    }
}