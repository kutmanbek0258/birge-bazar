package kg.birge.bazar.productservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVariantDto {
    private Long id;
    private String skuCode;
    private String attributesJson; // In a real app, you might want to map this to a Map<String, String>
    private BigDecimal priceModifier;
    private Integer stockQuantityFbs;
    private String imageUrl;
    private Boolean isActive;
}
