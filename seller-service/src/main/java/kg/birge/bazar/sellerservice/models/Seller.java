package kg.birge.bazar.sellerservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.sellerservice.config.audit.AuditableCustom;
import kg.birge.bazar.sellerservice.models.enums.SellerStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "sellers")
@Getter
@Setter
public class Seller extends AuditableCustom<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private UUID userId; // Logical FK to user-service

    @Column(name = "shop_name", unique = true, nullable = false)
    private String shopName;

    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SellerStatus status = SellerStatus.PENDING_APPROVAL;

    @Column(name = "commission_rate", precision = 5, scale = 4)
    private BigDecimal commissionRate;

    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "bank_details_json", columnDefinition = "jsonb")
    private String bankDetailsJson;

    @Column(name = "approval_date")
    private ZonedDateTime approvalDate;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SellerStaff> staff;
}