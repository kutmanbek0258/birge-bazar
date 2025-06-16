package kg.birge.bazar.productservice.dto;

import java.math.BigDecimal;

public class ProductDto extends AbstractDto<Long> {
    private Long id;
    private Long sellerId;
    private Long categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String status;
    private Integer fbsStock;
    private Integer fboStock;
    private String fulfillmentSchema;

    public ProductDto() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getSellerId() {
        return this.sellerId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setPrice(java.math.BigDecimal price) {
        this.price = price;
    }

    public java.math.BigDecimal getPrice() {
        return this.price;
    }

    public void setDiscountPrice(java.math.BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public java.math.BigDecimal getDiscountPrice() {
        return this.discountPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public void setFbsStock(Integer fbsStock) {
        this.fbsStock = fbsStock;
    }

    public Integer getFbsStock() {
        return this.fbsStock;
    }

    public void setFboStock(Integer fboStock) {
        this.fboStock = fboStock;
    }

    public Integer getFboStock() {
        return this.fboStock;
    }

    public void setFulfillmentSchema(String fulfillmentSchema) {
        this.fulfillmentSchema = fulfillmentSchema;
    }

    public String getFulfillmentSchema() {
        return this.fulfillmentSchema;
    }
}