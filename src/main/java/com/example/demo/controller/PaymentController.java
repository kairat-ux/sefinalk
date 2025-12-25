// ПУТЬ: src/main/java/com/example/demo/controller/PaymentController.java

package com.example.demo.controller;

import com.example.demo.dto.request.PaymentCreateRequestDTO;
import com.example.demo.dto.response.PaymentResponseDTO;
import com.example.demo.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @Valid @RequestBody PaymentCreateRequestDTO request) {
        // userId будет получен из Spring Security context
        PaymentResponseDTO payment = paymentService.createPayment(request, 1L);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id) {
        PaymentResponseDTO payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByReservationId(@PathVariable Long reservationId) {
        PaymentResponseDTO payment = paymentService.getPaymentByReservationId(reservationId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponseDTO>> getUserPayments(@PathVariable Long userId) {
        List<PaymentResponseDTO> payments = paymentService.getUserPayments(userId);
        return ResponseEntity.ok(payments);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> completePayment(@PathVariable Long id) {
        paymentService.completePayment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/refund")
    public ResponseEntity<Void> refundPayment(@PathVariable Long id,
                                              @RequestParam(required = false) String reason) {
        paymentService.refundPayment(id, reason != null ? reason : "Возврат пользователем");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservation/{reservationId}/refund-amount")
    public ResponseEntity<BigDecimal> calculateRefundAmount(@PathVariable Long reservationId) {
        BigDecimal refundAmount = paymentService.calculateRefundAmount(reservationId);
        return ResponseEntity.ok(refundAmount);
    }
}