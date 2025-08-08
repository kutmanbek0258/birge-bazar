package kg.birge.bazar.productservice.dto;

import lombok.Data;

@Data
public class ProductImageDto {
    private Long id;
    private String url;
    private Boolean isMain;
    private Integer orderIndex;
}
