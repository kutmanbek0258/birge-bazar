package kg.birge.bazar.productservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.productservice.config.audit.AuditableCustom;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Table(name = "products", schema = "products")
@Entity
@Audited
@Setter
@Getter
public class Product extends AuditableCustom<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "seller_id", nullable = false)
    private String sellerId;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @Audited(targetAuditMode = NOT_AUDITED)
    private Category category;

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
}