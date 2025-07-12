// domain/StockMovement.java
package kg.birge.bazar.wmsservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.wmsservice.config.audit.AuditableCustom;
import kg.birge.bazar.wmsservice.models.enums.ReferenceDocumentType;
import kg.birge.bazar.wmsservice.models.enums.StockMovementType;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

/**
 * Сущность "Движение запаса".
 * Журнал всех перемещений товаров внутри склада или при поступлении/отгрузке.
 */
@Entity
@Table(schema = "wms", name = "stock_movements")
@Audited // Включаем аудит
@NoArgsConstructor // Lombok для автоматической генерации конструктора без параметров
@AllArgsConstructor // Lombok для автоматической генерации конструктора со всеми параметрами
@Getter // Lombok для автоматической генерации геттеров
@Setter // Lombok для автоматической генерации сеттеров
@ToString // Lombok для автоматической генерации метода toString
public class StockMovement extends AuditableCustom<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_id")
    private Long movementId;

    @Column(name = "inventory_item_id")
    private Long inventoryItemId; // ID конкретной складской позиции (опционально, если отслеживается)

    @Column(name = "sku_code", nullable = false, length = 100)
    private String skuCode; // SKU товара

    @Column(name = "quantity", nullable = false)
    private Integer quantity; // Количество перемещенного товара (может быть отрицательным для OUT)

    @Column(name = "from_location_id")
    private Long fromLocationId; // ID исходной ячейки (логический FK к WMS.locations)

    @Column(name = "to_location_id")
    private Long toLocationId; // ID целевой ячейки (логический FK к WMS.locations)

    @Enumerated(EnumType.STRING) // Храним ENUM как строку
    @Column(name = "movement_type", nullable = false)
    private StockMovementType movementType; // Тип движения (приемка, отбор, перемещение и т.д.)

    @Column(name = "reference_document_id")
    private Long referenceDocumentId; // ID связанного документа (заказ, поставка, инвентаризация)

    @Enumerated(EnumType.STRING) // Храним ENUM как строку
    @Column(name = "reference_document_type")
    private ReferenceDocumentType referenceDocumentType; // Тип связанного документа

    @Column(name = "user_id", nullable = false)
    private Long userId; // ID пользователя, инициировавшего движение (логический FK к User/Admin Service)

    @Column(name = "moved_at", nullable = false)
    private LocalDateTime movedAt; // Время движения
}
