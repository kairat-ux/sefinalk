package com.example.demo.service;

import com.example.demo.dto.request.ReviewCreateRequestDTO;
import com.example.demo.dto.response.ReviewResponseDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.impl.ReviewServiceImpl;
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
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private ReviewCreateRequestDTO reviewDTO;
    private User user;
    private Restaurant restaurant;
    private Reservation reservation;
    private Review review;

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
                .rating(BigDecimal.ZERO)
                .totalReviews(0)
                .isActive(true)
                .build();


        reservation = Reservation.builder()
                .id(1L)
                .user(user)
                .restaurant(restaurant)
                .reservationDate(LocalDate.now().minusDays(1))
                .startTime(LocalTime.of(19, 0, 0))
                .endTime(LocalTime.of(21, 0, 0))
                .guestCount(4)
                .status(Reservation.ReservationStatus.COMPLETED)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();

        reviewDTO = ReviewCreateRequestDTO.builder()
                .rating(5)
                .comment("Excellent service and delicious food!")
                .build();

        review = Review.builder()
                .id(1L)
                .user(user)
                .restaurant(restaurant)
                .reservation(reservation)
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .isApproved(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateReview_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(restaurant));
        when(reviewRepository.findByRestaurantIdAndIsApprovedTrue(anyLong()))
                .thenReturn(Arrays.asList(review));

        ReviewResponseDTO result = reviewService.createReview(reviewDTO, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(5, result.getRating());
        assertEquals("Excellent service and delicious food!", result.getComment());
        assertFalse(result.getIsApproved());

        verify(userRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testCreateReview_ReservationNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reviewService.createReview(reviewDTO, 1L));

        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testCreateReview_ReservationNotCompleted() {
        reservation.setStatus(Reservation.ReservationStatus.PENDING);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        assertThrows(RuntimeException.class, () -> reviewService.createReview(reviewDTO, 1L));

        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void testGetReviewById_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        ReviewResponseDTO result = reviewService.getReviewById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(5, result.getRating());

        verify(reviewRepository, times(1)).findById(1L);
    }

    @Test
    void testGetReviewById_NotFound() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reviewService.getReviewById(1L));

        verify(reviewRepository, times(1)).findById(1L);
    }

    @Test
    void testGetRestaurantReviews_Success() {
        Review review2 = Review.builder()
                .id(2L)
                .user(user)
                .restaurant(restaurant)
                .reservation(reservation)
                .rating(4)
                .comment("Good experience")
                .isApproved(true)
                .build();

        when(reviewRepository.findByRestaurantIdAndIsApprovedTrue(1L))
                .thenReturn(Arrays.asList(review, review2));

        List<ReviewResponseDTO> result = reviewService.getRestaurantReviews(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(5, result.get(0).getRating());
        assertEquals(4, result.get(1).getRating());

        verify(reviewRepository, times(1)).findByRestaurantIdAndIsApprovedTrue(1L);
    }

    @Test
    void testGetUserReviews_Success() {
        when(reviewRepository.findByUserId(1L))
                .thenReturn(Arrays.asList(review));

        List<ReviewResponseDTO> result = reviewService.getUserReviews(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());

        verify(reviewRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetPendingReviews_Success() {
        when(reviewRepository.findByIsApprovedFalse())
                .thenReturn(Arrays.asList(review));

        List<ReviewResponseDTO> result = reviewService.getPendingReviews();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).getIsApproved());

        verify(reviewRepository, times(1)).findByIsApprovedFalse();
    }

    @Test
    void testApproveReview_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        reviewService.approveReview(1L);

        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testRejectReview_Success() {
        review.setIsApproved(true);
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        reviewService.rejectReview(1L);

        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testDeleteReview_Success() {
        reviewService.deleteReview(1L);

        verify(reviewRepository, times(1)).deleteById(1L);
    }
}