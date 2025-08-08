package kg.birge.bazar.productservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {

    @Size(max = 255)
    private String name;

    private String description;

    @Size(max = 500)
    private String shortDescription;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal basePrice;

    private Long categoryId;

    private Long brandId;

    private String mainImageUrl;
}
