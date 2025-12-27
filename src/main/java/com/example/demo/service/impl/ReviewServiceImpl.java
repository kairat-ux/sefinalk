// ПУТЬ: src/main/java/com/example/demo/service/impl/ReviewServiceImpl.java

package com.example.demo.service.impl;

import com.example.demo.dto.request.ReviewCreateRequestDTO;
import com.example.demo.dto.request.ReviewUpdateRequestDTO;
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
    private final NotificationRepository notificationRepository;

    @Override
    public ReviewResponseDTO createReview(ReviewCreateRequestDTO request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Ресторан не найден"));

        Review review = Review.builder()
                .user(user)
                .restaurant(restaurant)
                .reservation(null)
                .rating(request.getRating())
                .comment(request.getComment())
                .isApproved(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);
        updateRestaurantRating(restaurant.getId());

        // Создаем нотификацию владельцу ресторана о новом отзыве
        createNewReviewNotification(savedReview);

        return mapToDTO(savedReview);
    }

    @Override
    public ReviewResponseDTO updateReview(Long id, ReviewUpdateRequestDTO request, Long userId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отзыв не найден"));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("Вы можете редактировать только свои отзывы");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(review);
        updateRestaurantRating(review.getRestaurant().getId());

        return mapToDTO(updatedReview);
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
    public List<ReviewResponseDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
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
        updateRestaurantRating(review.getRestaurant().getId());
    }

    @Override
    public void rejectReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отзыв не найден"));
        review.setIsApproved(false);
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(review);
        updateRestaurantRating(review.getRestaurant().getId());
    }

    @Override
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Отзыв не найден"));
        Long restaurantId = review.getRestaurant().getId();
        reviewRepository.deleteById(id);
        updateRestaurantRating(restaurantId);
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

    private void createNewReviewNotification(Review review) {
        try {
            Restaurant restaurant = review.getRestaurant();
            User owner = restaurant.getOwner();

            String stars = "⭐".repeat(review.getRating());

            Notification notification = Notification.builder()
                    .user(owner)
                    .restaurant(restaurant)
                    .reservation(review.getReservation())
                    .title("New Review Received")
                    .message(String.format(
                            "%s left a %d-star review (%s) for %s: \"%s\"",
                            review.getUser().getFirstName() + " " + review.getUser().getLastName(),
                            review.getRating(),
                            stars,
                            restaurant.getName(),
                            review.getComment() != null && review.getComment().length() > 100
                                ? review.getComment().substring(0, 97) + "..."
                                : review.getComment()
                    ))
                    .type(Notification.NotificationType.REVIEW_REQUEST)
                    .isRead(false)
                    .deliveryStatus(Notification.DeliveryStatus.SENT)
                    .createdAt(LocalDateTime.now())
                    .sentAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
        } catch (Exception e) {
            System.err.println("Failed to create new review notification: " + e.getMessage());
        }
    }

    private ReviewResponseDTO mapToDTO(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .userId(review.getUser().getId())
                .restaurantId(review.getRestaurant().getId())
                .reservationId(review.getReservation() != null ? review.getReservation().getId() : null)
                .rating(review.getRating())
                .comment(review.getComment())
                .isApproved(review.getIsApproved())
                .createdAt(review.getCreatedAt())
                .build();
    }
}