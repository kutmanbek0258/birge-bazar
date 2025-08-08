package kg.birge.bazar.productservice.dto;

import kg.birge.bazar.productservice.model.enums.ProductStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private Long sellerId;
    private String name;
    private String description;
    private String shortDescription;
    private BigDecimal basePrice;
    private ProductStatus status;
    private String mainImageUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // Denormalized fields for convenience
    private Long categoryId;
    private String categoryName;
    private Long brandId;
    private String brandName;

    private List<ProductImageDto> images;
    private List<ProductVariantDto> variants;
}
