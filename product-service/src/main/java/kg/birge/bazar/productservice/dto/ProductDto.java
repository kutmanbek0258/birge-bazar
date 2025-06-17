package kg.birge.bazar.productservice.dto;

import kg.birge.bazar.productservice.models.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class ProductDto extends AbstractDto<Long> {
    private Long id;
    private String sellerId;
    private Category category;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String status;
    private Integer fbsStock;
    private Integer fboStock;
    private String fulfillmentSchema;
}