package kg.birge.bazar.reviewservice.controller;

import kg.birge.bazar.reviewservice.dto.CreateReviewRequest;
import kg.birge.bazar.reviewservice.dto.ReviewDto;
import kg.birge.bazar.reviewservice.dto.ReviewSummaryDto;
import kg.birge.bazar.reviewservice.dto.UpdateReviewRequest;
import kg.birge.bazar.reviewservice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    private UUID getUserId(OAuth2AuthenticatedPrincipal principal) {
        String userIdStr = principal.getAttribute("sub");
        if (userIdStr == null) {
            throw new IllegalArgumentException("User ID not found in token");
        }
        return UUID.fromString(userIdStr);
    }

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody CreateReviewRequest request,
                                                  @AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal) {
        ReviewDto createdReview = reviewService.createReview(request, getUserId(principal));
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ReviewDto>> getReviewsByProductId(@PathVariable Long productId, Pageable pageable) {
        Page<ReviewDto> reviews = reviewService.getReviewsByProductId(productId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/product/{productId}/summary")
    public ResponseEntity<ReviewSummaryDto> getReviewSummary(@PathVariable Long productId) {
        ReviewSummaryDto summary = reviewService.getReviewSummary(productId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/user/me")
    public ResponseEntity<Page<ReviewDto>> getMyReviews(Pageable pageable,
                                                        @AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal) {
        Page<ReviewDto> reviews = reviewService.getReviewsByUserId(getUserId(principal), pageable);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long reviewId,
                                                  @Valid @RequestBody UpdateReviewRequest request,
                                                  @AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal) {
        ReviewDto updatedReview = reviewService.updateReview(reviewId, request, getUserId(principal));
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId,
                                             @AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal) {
        reviewService.deleteReview(reviewId, getUserId(principal));
        return ResponseEntity.noContent().build();
    }
}
