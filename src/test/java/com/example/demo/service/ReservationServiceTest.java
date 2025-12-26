package com.example.demo.service;

import com.example.demo.dto.request.ReservationCreateRequestDTO;
import com.example.demo.dto.response.ReservationResponseDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantTableRepository tableRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private ReservationCreateRequestDTO reservationDTO;
    private User user;
    private Restaurant restaurant;
    private RestaurantTable table;
    private Reservation reservation;

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

        table = RestaurantTable.builder()
                .id(1L)
                .restaurant(restaurant)
                .tableNumber(1)
                .capacity(4)
                .location(RestaurantTable.TableLocation.CENTER)
                .isActive(true)
                .build();

        reservationDTO = ReservationCreateRequestDTO.builder()
                .restaurantId(1L)
                .reservationDate(LocalDate.now().plusDays(2))
                .startTime(LocalTime.of(19, 0, 0))
                .endTime(LocalTime.of(21, 0, 0))
                .guestCount(4)
                .specialRequests("Window seat")
                .build();

        reservation = Reservation.builder()
                .id(1L)
                .user(user)
                .restaurant(restaurant)
                .table(table)
                .reservationDate(reservationDTO.getReservationDate())
                .startTime(reservationDTO.getStartTime())
                .endTime(reservationDTO.getEndTime())
                .guestCount(reservationDTO.getGuestCount())
                .specialRequests(reservationDTO.getSpecialRequests())
                .status(Reservation.ReservationStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateReservation_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
        when(tableRepository.findByRestaurantIdAndIsActiveTrue(anyLong()))
                .thenReturn(Arrays.asList(table));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        ReservationResponseDTO result = reservationService.createReservation(reservationDTO, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getRestaurantId());
        assertEquals(Reservation.ReservationStatus.CONFIRMED.toString(), result.getStatus());

        verify(userRepository, times(1)).findById(1L);
        verify(restaurantRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                reservationService.createReservation(reservationDTO, 1L));

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_RestaurantNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                reservationService.createReservation(reservationDTO, 1L));

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testGetReservationById_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        ReservationResponseDTO result = reservationService.getReservationById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(4, result.getGuestCount());

        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetReservationById_NotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reservationService.getReservationById(1L));

        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserReservations_Success() {
        Reservation reservation2 = Reservation.builder()
                .id(2L)
                .user(user)
                .restaurant(restaurant)
                .table(table)
                .reservationDate(LocalDate.now().plusDays(3))
                .startTime(LocalTime.of(20, 0, 0))
                .status(Reservation.ReservationStatus.CONFIRMED)
                .build();

        when(reservationRepository.findUserReservations(1L))
                .thenReturn(Arrays.asList(reservation, reservation2));

        List<ReservationResponseDTO> result = reservationService.getUserReservations(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(reservationRepository, times(1)).findUserReservations(1L);
    }

    @Test
    void testGetRestaurantReservations_Success() {
        when(reservationRepository.findByRestaurantId(1L))
                .thenReturn(Arrays.asList(reservation));

        List<ReservationResponseDTO> result = reservationService.getRestaurantReservations(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getRestaurantId());

        verify(reservationRepository, times(1)).findByRestaurantId(1L);
    }

    @Test
    void testCancelReservation_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        reservationService.cancelReservation(1L, "User request");

        verify(reservationRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testIsTableAvailable_Available() {
        when(reservationRepository.findByTableId(1L))
                .thenReturn(Arrays.asList());

        boolean result = reservationService.isTableAvailable(1L, LocalDate.now().plusDays(2));

        assertTrue(result);

        verify(reservationRepository, times(1)).findByTableId(1L);
    }

    @Test
    void testIsTableAvailable_NotAvailable() {
        LocalDate targetDate = LocalDate.now().plusDays(2);
        Reservation existingReservation = Reservation.builder()
                .id(1L)
                .user(user)
                .restaurant(restaurant)
                .table(table)
                .reservationDate(targetDate)
                .status(Reservation.ReservationStatus.CONFIRMED)
                .build();

        when(reservationRepository.findByTableId(1L))
                .thenReturn(Arrays.asList(existingReservation));

        boolean result = reservationService.isTableAvailable(1L, targetDate);

        assertFalse(result);

        verify(reservationRepository, times(1)).findByTableId(1L);
    }

    @Test
    void testUpdateReservation_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        ReservationCreateRequestDTO updateDTO = ReservationCreateRequestDTO.builder()
                .restaurantId(1L)
                .reservationDate(LocalDate.now().plusDays(3))
                .startTime(LocalTime.of(20, 0, 0))
                .endTime(LocalTime.of(22, 0, 0))
                .guestCount(6)
                .build();

        reservationService.updateReservation(1L, updateDTO);

        verify(reservationRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }
}