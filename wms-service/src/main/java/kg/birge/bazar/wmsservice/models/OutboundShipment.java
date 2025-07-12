package kg.birge.bazar.wmsservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.wmsservice.config.audit.AuditableCustom;
import kg.birge.bazar.wmsservice.models.enums.OutboundStatus;

import java.time.OffsetDateTime;

@Table(name = "outbound_shipments", schema = "wms")
@Entity
public class OutboundShipment extends AuditableCustom<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outbound_id", nullable = false)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "carrier_id")
    private Long carrierId;

    @Column(name = "tracking_number")
    private String trackingNumber; // e.g., "1Z999AA10123456784"

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OutboundStatus status; // e.g., PENDING, SHIPPED, DELIVERED

    @Column(name = "dispatch_at")
    private OffsetDateTime dispatchAt; // e.g., "2023-10-01T10:15:30+01:00"
}