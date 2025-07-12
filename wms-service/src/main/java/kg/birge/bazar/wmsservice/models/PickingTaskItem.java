// domain/PickingTaskItem.java
package kg.birge.bazar.wmsservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.wmsservice.config.audit.AuditableCustom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

/**
 * Сущность "Позиция задания на отбор".
 * Детализирует, какие товары и в каком количестве должны быть отобраны
 * в рамках задания на отбор.
 */
@Entity
@Table(schema = "wms", name = "picking_task_items")
@Audited // Включаем аудит
@NoArgsConstructor // Lombok аннотация для генерации конструктора без параметров
@AllArgsConstructor // Lombok аннотация для генерации конструктора со всеми параметрами
@Getter // Lombok аннотация для генерации геттеров
@Setter // Lombok аннотация для генерации сеттеров
public class PickingTaskItem extends AuditableCustom<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_item_id")
    private Long taskItemId;

    @ManyToOne(fetch = FetchType.LAZY) // Связь "многие к одному" с PickingTask
    @JoinColumn(name = "task_id", nullable = false) // Колонка внешнего ключа
    private PickingTask pickingTask;

    @Column(name = "sku_code", nullable = false, length = 100)
    private String skuCode; // SKU товара

    @Column(name = "quantity_to_pick", nullable = false)
    private Integer quantityToPick; // Количество, которое нужно отобрать

    @Column(name = "picked_quantity", nullable = false)
    private Integer pickedQuantity; // Фактически отобранное количество

    @Column(name = "source_location_id")
    private Long sourceLocationId; // ID рекомендуемой ячейки для отбора (логический FK к WMS.locations)

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "taskItemId = " + taskItemId + ", " +
                "createdBy = " + createdBy + ", " +
                "createdDate = " + createdDate + ", " +
                "lastModifiedBy = " + lastModifiedBy + ", " +
                "lastModifiedDate = " + lastModifiedDate + ", " +
                "skuCode = " + skuCode + ", " +
                "quantityToPick = " + quantityToPick + ", " +
                "pickedQuantity = " + pickedQuantity + ", " +
                "sourceLocationId = " + sourceLocationId + ")";
    }
}
