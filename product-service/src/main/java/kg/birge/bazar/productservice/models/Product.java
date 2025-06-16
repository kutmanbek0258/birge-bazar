package kg.birge.bazar.productservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.productservice.config.audit.AuditableCustom;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.Instant;

@Table(name = "products", schema = "products")
@Entity
@Audited
public class Product extends AuditableCustom<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "discount_price", precision = 12, scale = 2)
    private BigDecimal discountPrice;

    @Column(name = "status", length = 32)
    private String status;

    @Column(name = "fbs_stock")
    private Integer fbsStock;

    @Column(name = "fbo_stock")
    private Integer fboStock;

    @Column(name = "fulfillment_schema", length = 8)
    private String fulfillmentSchema;

    public String getFulfillmentSchema() {
        return fulfillmentSchema;
    }

    public void setFulfillmentSchema(String fulfillmentSchema) {
        this.fulfillmentSchema = fulfillmentSchema;
    }

    public Integer getFboStock() {
        return fboStock;
    }

    public void setFboStock(Integer fboStock) {
        this.fboStock = fboStock;
    }

    public Integer getFbsStock() {
        return fbsStock;
    }

    public void setFbsStock(Integer fbsStock) {
        this.fbsStock = fbsStock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}