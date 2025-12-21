// ПУТЬ: src/main/java/com/example/demo/service/PaymentService.java

package com.example.demo.service;

import com.example.demo.dto.request.PaymentCreateRequestDTO;
import com.example.demo.dto.response.PaymentResponseDTO;
import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentCreateRequestDTO request, Long userId);
    PaymentResponseDTO getPaymentById(Long id);
    PaymentResponseDTO getPaymentByReservationId(Long reservationId);
    List<PaymentResponseDTO> getUserPayments(Long userId);
    void completePayment(Long id);
    void refundPayment(Long id, String reason);
    BigDecimal calculateRefundAmount(Long reservationId);
}