// domain/Location.java
package kg.birge.bazar.wmsservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.wmsservice.config.audit.AuditableCustom;
import kg.birge.bazar.wmsservice.models.enums.LocationType;
import lombok.*;
import org.hibernate.envers.Audited;

/**
 * Сущность "Место хранения" (ячейка).
 * Представляет конкретное место для хранения товаров внутри склада.
 */
@Entity
@Table(schema = "wms", name = "locations", uniqueConstraints = @UniqueConstraint(columnNames = {"warehouse_id", "code"}))
@Audited // Включаем аудит для этой сущности
@NoArgsConstructor // Lombok для автоматической генерации конструктора без параметров
@AllArgsConstructor // Lombok для автоматической генерации конструктора со всеми параметрами
@Getter // Lombok для автоматической генерации геттеров
@Setter // Lombok для автоматической генерации сеттеров
@ToString // Lombok для автоматической генерации метода toString
public class Location extends AuditableCustom<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId; // ID склада, к которому относится ячейка (логический FK)

    @Column(name = "code", nullable = false, length = 100)
    private String code; // Уникальный код ячейки в рамках склада (например, "A-01-01")

    @Enumerated(EnumType.STRING) // Храним ENUM как строку
    @Column(name = "type", nullable = false)
    private LocationType type; // Тип ячейки (полка, паллет и т.д.)

    @Column(name = "max_capacity_units")
    private Integer maxCapacityUnits; // Максимальная вместимость в единицах товара

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // Активна ли ячейка
}