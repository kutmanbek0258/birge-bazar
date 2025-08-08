package kg.birge.bazar.sellerservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.sellerservice.config.audit.AuditableCustom;
import kg.birge.bazar.sellerservice.models.enums.SellerStaffRole;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "seller_staff")
@Getter
@Setter
public class SellerStaff extends AuditableCustom<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @Column(name = "user_id", unique = true, nullable = false)
    private UUID userId; // Logical FK to user-service

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SellerStaffRole role;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "accepted_at")
    private ZonedDateTime acceptedAt;
}