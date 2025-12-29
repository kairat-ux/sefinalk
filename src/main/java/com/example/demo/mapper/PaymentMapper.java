package com.example.demo.mapper;

import com.example.demo.dto.request.PaymentCreateRequestDTO;
import com.example.demo.dto.response.PaymentResponseDTO;
import com.example.demo.entity.Payment;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponseDTO toResponseDTO(Payment payment) {
        if (payment == null) {
            return null;
        }

        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .reservationId(payment.getReservation() != null ? payment.getReservation().getId() : null)
                .userId(payment.getUser() != null ? payment.getUser().getId() : null)
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod() != null ? payment.getPaymentMethod().name() : null)
                .status(payment.getStatus() != null ? payment.getStatus().name() : null)
                .transactionId(payment.getTransactionId())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public Payment toEntity(PaymentCreateRequestDTO dto, User user, Reservation reservation) {
        if (dto == null || user == null || reservation == null) {
            return null;
        }

        Payment.PaymentMethod paymentMethod = Payment.PaymentMethod.valueOf(dto.getPaymentMethod().toUpperCase());

        return Payment.builder()
                .reservation(reservation)
                .user(user)
                .amount(dto.getAmount())
                .currency("KZT")
                .paymentMethod(paymentMethod)
                .status(Payment.PaymentStatus.PENDING)
                .build();
    }
}
