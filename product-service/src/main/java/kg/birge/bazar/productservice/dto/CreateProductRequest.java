package kg.birge.bazar.productservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {

    @NotEmpty
    @Size(max = 255)
    private String name;

    @NotEmpty
    private String description;

    @Size(max = 500)
    private String shortDescription;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal basePrice;

    @NotNull
    private Long categoryId;

    private Long brandId;

    @NotEmpty
    private String mainImageUrl;
}
