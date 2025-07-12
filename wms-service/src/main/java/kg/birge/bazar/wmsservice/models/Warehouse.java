// domain/Warehouse.java
package kg.birge.bazar.wmsservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.wmsservice.config.audit.AuditableCustom;
import lombok.*;
import org.hibernate.envers.Audited;

/**
 * Сущность "Склад".
 * Представляет физический склад, управляемый WMS.
 */
@Entity
@Table(schema = "wms", name = "warehouses")
@Audited // Включаем аудит для этой сущности
@NoArgsConstructor // Lombok для автоматической генерации конструктора без параметров
@AllArgsConstructor // Lombok для автоматической генерации конструктора со всеми параметрами
@Getter // Lombok для автоматической генерации геттеров
@Setter // Lombok для автоматической генерации сеттеров
@ToString // Lombok для автоматической генерации метода toString
public class Warehouse extends AuditableCustom<Long> { // Наследуем от Auditable для полей аудита

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация ID (BIGSERIAL в PostgreSQL)
    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "address", nullable = false, columnDefinition = "TEXT") // TEXT для длинных адресов
    private String address;

    @Column(name = "capacity_sq_m")
    private Double capacitySqM; // Площадь склада в квадратных метрах

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true; // Активен ли склад
}
