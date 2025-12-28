package com.example.demo.service;

import com.example.demo.dto.request.PaymentCreateRequestDTO;
import com.example.demo.dto.response.PaymentResponseDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentCreateRequestDTO paymentDTO;
    private User user;
    private Restaurant restaurant;
    private Reservation reservation;
    private Payment payment;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .role(User.UserRole.USER)
                .isActive(true)
                .isBlocked(false)
                .build();

        restaurant = Restaurant.builder()
                .id(1L)
                .owner(user)
                .name("Test Restaurant")
                .address("123 Main St")
                .city("Almaty")
                .isActive(true)
                .build();


        reservation = Reservation.builder()
                .id(1L)
                .user(user)
                .restaurant(restaurant)
                .reservationDate(LocalDate.now().plusDays(2))
                .startTime(LocalTime.of(19, 0, 0))
                .endTime(LocalTime.of(21, 0, 0))
                .guestCount(4)
                .status(Reservation.ReservationStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        paymentDTO = PaymentCreateRequestDTO.builder()
                .reservationId(1L)
                .amount(new BigDecimal("15000.00"))
                .paymentMethod("CARD")
                .build();

        payment = Payment.builder()
                .id(1L)
                .reservation(reservation)
                .user(user)
                .amount(new BigDecimal("15000.00"))
                .currency("KZT")
                .paymentMethod(Payment.PaymentMethod.CARD)
                .status(Payment.PaymentStatus.PENDING)
                .transactionId("TXN-12345")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreatePayment_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponseDTO result = paymentService.createPayment(paymentDTO, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(new BigDecimal("15000.00"), result.getAmount());
        assertEquals("PENDING", result.getStatus());
        assertEquals("KZT", result.getCurrency());

        verify(userRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testCreatePayment_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> paymentService.createPayment(paymentDTO, 1L));

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testCreatePayment_ReservationNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> paymentService.createPayment(paymentDTO, 1L));

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testGetPaymentById_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentResponseDTO result = paymentService.getPaymentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("CARD", result.getPaymentMethod());

        verify(paymentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPaymentById_NotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> paymentService.getPaymentById(1L));

        verify(paymentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPaymentByReservationId_Success() {
        when(paymentRepository.findByReservationId(1L)).thenReturn(Optional.of(payment));

        PaymentResponseDTO result = paymentService.getPaymentByReservationId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getReservationId());

        verify(paymentRepository, times(1)).findByReservationId(1L);
    }

    @Test
    void testGetUserPayments_Success() {
        Payment payment2 = Payment.builder()
                .id(2L)
                .reservation(reservation)
                .user(user)
                .amount(new BigDecimal("20000.00"))
                .currency("KZT")
                .paymentMethod(Payment.PaymentMethod.CARD)
                .status(Payment.PaymentStatus.COMPLETED)
                .transactionId("TXN-67890")
                .build();

        when(paymentRepository.findByUserId(1L))
                .thenReturn(Arrays.asList(payment, payment2));

        List<PaymentResponseDTO> result = paymentService.getUserPayments(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(new BigDecimal("15000.00"), result.get(0).getAmount());
        assertEquals(new BigDecimal("20000.00"), result.get(1).getAmount());

        verify(paymentRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testCompletePayment_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        paymentService.completePayment(1L);

        verify(paymentRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testRefundPayment_Success() {
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        paymentService.refundPayment(1L, "User request");

        verify(paymentRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testRefundPayment_NotCompleted() {
        payment.setStatus(Payment.PaymentStatus.PENDING);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        assertThrows(RuntimeException.class, () -> paymentService.refundPayment(1L, "User request"));

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testCalculateRefundAmount_100Percent() {
        LocalDateTime reservationTime = LocalDateTime.now().plusHours(48);
        Reservation futureReservation = Reservation.builder()
                .id(1L)
                .user(user)
                .restaurant(restaurant)
                .reservationDate(reservationTime.toLocalDate())
                .startTime(reservationTime.toLocalTime())
                .status(Reservation.ReservationStatus.CONFIRMED)
                .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(futureReservation));
        when(paymentRepository.findByReservationId(1L)).thenReturn(Optional.of(payment));

        BigDecimal result = paymentService.calculateRefundAmount(1L);

        assertEquals(new BigDecimal("15000.00"), result);

        verify(reservationRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).findByReservationId(1L);
    }

    @Test
    void testCalculateRefundAmount_50Percent() {
        LocalDateTime reservationTime = LocalDateTime.now().plusHours(12);
        Reservation futureReservation = Reservation.builder()
                .id(1L)
                .user(user)
                .restaurant(restaurant)
                .reservationDate(reservationTime.toLocalDate())
                .startTime(reservationTime.toLocalTime())
                .status(Reservation.ReservationStatus.CONFIRMED)
                .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(futureReservation));
        when(paymentRepository.findByReservationId(1L)).thenReturn(Optional.of(payment));

        BigDecimal result = paymentService.calculateRefundAmount(1L);

        assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(result.compareTo(payment.getAmount()) < 0);

        verify(reservationRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).findByReservationId(1L);
    }

    @Test
    void testCalculateRefundAmount_Zero() {
        LocalDateTime reservationTime = LocalDateTime.now().plusHours(1);
        Reservation futureReservation = Reservation.builder()
                .id(1L)
                .user(user)
                .restaurant(restaurant)
                .reservationDate(reservationTime.toLocalDate())
                .startTime(reservationTime.toLocalTime())
                .status(Reservation.ReservationStatus.CONFIRMED)
                .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(futureReservation));
        when(paymentRepository.findByReservationId(1L)).thenReturn(Optional.of(payment));

        BigDecimal result = paymentService.calculateRefundAmount(1L);

        assertEquals(BigDecimal.ZERO, result);

        verify(reservationRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).findByReservationId(1L);
    }
}