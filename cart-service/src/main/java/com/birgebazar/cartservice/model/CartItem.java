package com.birgebazar.cartservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "cart_items", schema = "cart_service")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming BIGSERIAL for cart_item_id
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false) // FK to Cart.cart_id
    private Cart cart;

    @Column(name = "product_id", nullable = false)
    private Long productId; // Logical FK to Product Service.Product.product_id

    @Column(name = "product_variant_id")
    private Long productVariantId; // Logical FK to Product Service.ProductVariant.variant_id

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price_at_add", nullable = false)
    private BigDecimal priceAtAdd;

    @Column(name = "added_at", nullable = false)
    private Instant addedAt;

    // AuditableCustom fields
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    // Constructors, getters, setters (Lombok handles these with @Data)
}
