package com.example.demo.service;

import com.example.demo.dto.request.ReviewCreateRequestDTO;
import com.example.demo.dto.request.ReviewUpdateRequestDTO;
import com.example.demo.dto.response.ReviewResponseDTO;
import java.util.List;

public interface ReviewService {
    ReviewResponseDTO createReview(ReviewCreateRequestDTO request, Long userId);
    ReviewResponseDTO updateReview(Long id, ReviewUpdateRequestDTO request, Long userId);
    ReviewResponseDTO getReviewById(Long id);
    List<ReviewResponseDTO> getRestaurantReviews(Long restaurantId);
    List<ReviewResponseDTO> getUserReviews(Long userId);
    List<ReviewResponseDTO> getAllReviews();
    List<ReviewResponseDTO> getPendingReviews();
    void approveReview(Long id);
    void rejectReview(Long id);
    void deleteReview(Long id);
}