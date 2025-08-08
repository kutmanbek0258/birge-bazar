package kg.birge.bazar.reviewservice.service;

import kg.birge.bazar.reviewservice.dto.CreateReviewRequest;
import kg.birge.bazar.reviewservice.dto.ReviewDto;
import kg.birge.bazar.reviewservice.dto.ReviewSummaryDto;
import kg.birge.bazar.reviewservice.dto.UpdateReviewRequest;
import kg.birge.bazar.reviewservice.models.Review;
import kg.birge.bazar.reviewservice.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewDto createReview(CreateReviewRequest request, UUID userId) {
        reviewRepository.findByProductIdAndUserId(request.getProductId(), userId)
                .ifPresent(review -> {
                    throw new IllegalStateException("You have already reviewed this product.");
                });

        Review review = new Review();
        review.setProductId(request.getProductId());
        review.setUserId(userId);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);
        return toDto(savedReview);
    }

    @Transactional(readOnly = true)
    public Page<ReviewDto> getReviewsByProductId(Long productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ReviewDto> getReviewsByUserId(UUID userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public ReviewSummaryDto getReviewSummary(Long productId) {
        Double averageRating = reviewRepository.getAverageRatingByProductId(productId).orElse(0.0);
        long totalReviews = reviewRepository.countByProductId(productId);
        return new ReviewSummaryDto(averageRating, totalReviews);
    }

    @Transactional
    public ReviewDto updateReview(Long reviewId, UpdateReviewRequest request, UUID userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));

        if (!review.getUserId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to update this review.");
        }

        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }
        if (request.getComment() != null) {
            review.setComment(request.getComment());
        }

        Review updatedReview = reviewRepository.save(review);
        return toDto(updatedReview);
    }

    @Transactional
    public void deleteReview(Long reviewId, UUID userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));

        // In a real app, you'd also check for admin role here
        if (!review.getUserId().equals(userId)) {
            throw new IllegalStateException("You are not authorized to delete this review.");
        }

        reviewRepository.delete(review);
    }

    private ReviewDto toDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setProductId(review.getProductId());
        dto.setUserId(review.getUserId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        return dto;
    }
}
