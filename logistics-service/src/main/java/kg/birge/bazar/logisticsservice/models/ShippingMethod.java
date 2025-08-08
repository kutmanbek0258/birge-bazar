package kg.birge.bazar.logisticsservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.logisticsservice.config.audit.AuditableCustom;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "shipping_methods", schema = "logistics")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShippingMethod extends AuditableCustom<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "method_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "base_cost", nullable = false)
    private BigDecimal baseCost;

    @Column(name = "min_delivery_days", nullable = false)
    private Integer minDeliveryDays;

    @Column(name = "max_delivery_days", nullable = false)
    private Integer maxDeliveryDays;

    @Column(name = "is_active")
    private boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "carrier_id")
    private ShippingCarrier carrier;

    @Column(name = "is_marketplace_delivery")
    private boolean isMarketplaceDelivery = false;
}
