package kg.birge.bazar.wmsservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.wmsservice.config.audit.AuditableCustom;
import kg.birge.bazar.wmsservice.models.enums.InventoryStatus;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

/**
 * Сущность "Складская позиция" / "Запас".
 * Отслеживает количество конкретного SKU в определенной ячейке на складе.
 */
@Entity
@Table(schema = "wms", name = "inventory_items", uniqueConstraints = @UniqueConstraint(columnNames = {"sku_code", "location_id", "batch_number"}))
@Audited // Включаем аудит для этой сущности
@NoArgsConstructor // Lombok для автоматической генерации конструктора без параметров
@AllArgsConstructor // Lombok для автоматической генерации конструктора со всеми параметрами
@Getter // Lombok для автоматической генерации геттеров
@Setter // Lombok для автоматической генерации сеттеров
@ToString // Lombok для автоматической генерации метода toString
public class InventoryItem extends AuditableCustom<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sku_code", nullable = false, length = 100)
    private String skuCode; // Код товара (логический FK к Product Service)

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId; // ID склада (логический FK к WMS.warehouses)

    @Column(name = "location_id", nullable = false)
    private Long locationId; // ID ячейки (логический FK к WMS.locations)

    @Column(name = "quantity", nullable = false)
    private Integer quantity; // Количество товара в этой ячейке

    @Column(name = "seller_id", nullable = false)
    private Long sellerId; // ID продавца (логический FK к Seller Service)

    @Enumerated(EnumType.STRING) // Храним ENUM как строку
    @Column(name = "status", nullable = false)
    private InventoryStatus status; // Статус инвентаря (доступен, зарезервирован и т.д.)

    @Column(name = "batch_number", length = 255)
    private String batchNumber; // Номер партии (опционально)

    @Column(name = "expiration_date")
    private LocalDate expirationDate; // Срок годности (опционально)
}

