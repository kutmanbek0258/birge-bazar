package kg.birge.bazar.logisticsservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.logisticsservice.config.audit.AuditableCustom;
import lombok.*;

@Entity
@Table(name = "shipping_carriers", schema = "logistics")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShippingCarrier extends AuditableCustom<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carrier_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "tracking_url_pattern")
    private String trackingUrlPattern;

    @Column(name = "api_key_encrypted")
    private String apiKeyEncrypted;

    @Column(name = "is_active")
    private boolean isActive = true;
}
