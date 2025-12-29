package com.example.demo.mapper;

import com.example.demo.dto.request.ReviewCreateRequestDTO;
import com.example.demo.dto.request.ReviewUpdateRequestDTO;
import com.example.demo.dto.response.ReviewResponseDTO;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponseDTO toResponseDTO(Review review) {
        if (review == null) {
            return null;
        }

        return ReviewResponseDTO.builder()
                .id(review.getId())
                .userId(review.getUser() != null ? review.getUser().getId() : null)
                .restaurantId(review.getRestaurant() != null ? review.getRestaurant().getId() : null)
                .reservationId(review.getReservation() != null ? review.getReservation().getId() : null)
                .rating(review.getRating())
                .comment(review.getComment())
                .isApproved(review.getIsApproved())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public Review toEntity(ReviewCreateRequestDTO dto, User user, Restaurant restaurant) {
        if (dto == null || user == null || restaurant == null) {
            return null;
        }

        return Review.builder()
                .user(user)
                .restaurant(restaurant)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .isApproved(false)
                .build();
    }

    public Review toEntity(ReviewCreateRequestDTO dto, User user, Restaurant restaurant, Reservation reservation) {
        if (dto == null || user == null || restaurant == null) {
            return null;
        }

        return Review.builder()
                .user(user)
                .restaurant(restaurant)
                .reservation(reservation)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .isApproved(false)
                .build();
    }

    public void updateEntityFromDTO(ReviewUpdateRequestDTO dto, Review review) {
        if (dto == null || review == null) {
            return;
        }

        if (dto.getRating() != null) {
            review.setRating(dto.getRating());
        }
        if (dto.getComment() != null) {
            review.setComment(dto.getComment());
        }
    }
}
