package kg.birge.bazar.wmsservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.wmsservice.models.enums.TransferStatus;

import java.time.OffsetDateTime;

@Table(name = "transfers", schema = "wms")
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id", nullable = false)
    private Long id;

    @Column(name = "sku_code", nullable = false, length = 100)
    private String skuCode;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "from_location_id", nullable = false)
    private Location fromLocation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "to_location_id", nullable = false)
    private Location toLocation;

    @Column(name = "initiated_by_user_id", nullable = false)
    private Long initiatedByUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransferStatus status; // e.g., PENDING, COMPLETED, CANCELLED

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;
}