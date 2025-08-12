package com.birgebazar.cartservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "carts", schema = "cart_service")
@Data
@EqualsAndHashCode(callSuper = false) // Assuming AuditableCustom is not directly extended but its fields are included
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming BIGSERIAL for cart_id
    private Long cartId;

    @Column(name = "user_id")
    private Long userId; // Logical FK to User Service.User.user_id. Nullable for anonymous carts.

    @Column(name = "session_id")
    private String sessionId; // Session ID for anonymous users

    @Column(name = "expires_at")
    private Instant expiresAt;

    // AuditableCustom fields
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items;

    // Constructors, getters, setters (Lombok handles these with @Data)
}
