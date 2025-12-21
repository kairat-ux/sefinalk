// ПУТЬ: src/main/java/com/example/demo/service/impl/ReviewServiceImpl.java

package com.example.demo.service.impl;

import com.example.demo.dto.request.ReviewCreateRequestDTO;
import com.example.demo.dto.response.ReviewResponseDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public ReviewResponseDTO createReview(ReviewCreateRequestDTO request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        if (reservation.getStatus() != Reservation.ReservationStatus.COMPLETED) {
            throw new RuntimeException("Отзыв можно оставить только после завершения бронирования");
        }

        Restaurant restaurant = reservation.getRestaurant();

        Review review = Review.builder()
                .user(user)
                .restaurant(restaurant)
                .reservation(reservation)
                .rating(request.getRating())
                .comment(request.getComment())
                .isApproved(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);
        updateRestaurantRating(restaurant.getId());

        return mapToDTO(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отзыв не найден"));
        return mapToDTO(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getRestaurantReviews(Long restaurantId) {
        return reviewRepository.findByRestaurantIdAndIsApprovedTrue(restaurantId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getUserReviews(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getPendingReviews() {
        return reviewRepository.findByIsApprovedFalse().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void approveReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отзыв не найден"));
        review.setIsApproved(true);
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    @Override
    public void rejectReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отзыв не найден"));
        review.setIsApproved(false);
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    private void updateRestaurantRating(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Ресторан не найден"));

        List<Review> approvedReviews = reviewRepository.findByRestaurantIdAndIsApprovedTrue(restaurantId);

        if (approvedReviews.isEmpty()) {
            restaurant.setRating(BigDecimal.ZERO);
            restaurant.setTotalReviews(0);
        } else {
            BigDecimal totalRating = approvedReviews.stream()
                    .map(r -> BigDecimal.valueOf(r.getRating()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal averageRating = totalRating.divide(
                    BigDecimal.valueOf(approvedReviews.size()),
                    2,
                    java.math.RoundingMode.HALF_UP
            );

            restaurant.setRating(averageRating);
            restaurant.setTotalReviews(approvedReviews.size());
        }

        restaurantRepository.save(restaurant);
    }

    private ReviewResponseDTO mapToDTO(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .userId(review.getUser().getId())
                .restaurantId(review.getRestaurant().getId())
                .reservationId(review.getReservation().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .isApproved(review.getIsApproved())
                .createdAt(review.getCreatedAt())
                .build();
    }
}