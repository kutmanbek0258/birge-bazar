package kg.birge.bazar.reviewservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateReviewRequest {

    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;
}
