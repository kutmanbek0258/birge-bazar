package kg.birge.bazar.logisticsservice.models;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import kg.birge.bazar.logisticsservice.config.audit.AuditableCustom;
import lombok.*;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "pickup_points", schema = "logistics")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PickupPoint extends AuditableCustom<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private BigDecimal latitude;

    @Column(nullable = false)
    private BigDecimal longitude;

    @Type(JsonType.class)
    @Column(name = "working_hours_json", columnDefinition = "jsonb")
    private Map<String, String> workingHours;

    @Column(name = "is_active")
    private boolean isActive = true;
}
