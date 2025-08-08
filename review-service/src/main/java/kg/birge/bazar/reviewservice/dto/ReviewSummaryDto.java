package kg.birge.bazar.reviewservice.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewSummaryDto {
    private Double averageRating;
    private Long totalReviews;
}
