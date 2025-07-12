// domain/PickingTask.java
package kg.birge.bazar.wmsservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.wmsservice.config.audit.AuditableCustom;
import kg.birge.bazar.wmsservice.models.enums.PickingTaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность "Задание на отбор".
 * Представляет собой задачу для WMS-оператора по сбору товаров для конкретного заказа покупателя.
 */
@Entity
@Table(schema = "wms", name = "picking_tasks")
@Audited // Включаем аудит
@NoArgsConstructor // Lombok аннотация для генерации конструктора без параметров
@AllArgsConstructor // Lombok аннотация для генерации конструктора со всеми параметрами
@Getter // Lombok аннотация для генерации геттеров
@Setter // Lombok аннотация для генерации сеттеров
// Lombok аннотация для генерации метода toString
public class PickingTask extends AuditableCustom<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "order_id", nullable = false)
    private Long orderId; // ID заказа (логический FK к Order Service.Order)

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId; // ID склада, где выполняется отбор (логический FK к WMS.warehouses)

    @Column(name = "picker_user_id")
    private Long pickerUserId; // ID пользователя-оператора, выполняющего отбор (логический FK к User Service.User)

    @Enumerated(EnumType.STRING) // Храним ENUM как строку
    @Column(name = "status", nullable = false)
    private PickingTaskStatus status; // Текущий статус задания

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt; // Время назначения задания

    @Column(name = "completed_at")
    private LocalDateTime completedAt; // Время завершения задания

    @OneToMany(mappedBy = "pickingTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PickingTaskItem> items = new ArrayList<>(); // Позиции, которые нужно отобрать

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "taskId = " + taskId + ", " +
                "createdBy = " + createdBy + ", " +
                "createdDate = " + createdDate + ", " +
                "lastModifiedBy = " + lastModifiedBy + ", " +
                "lastModifiedDate = " + lastModifiedDate + ", " +
                "orderId = " + orderId + ", " +
                "warehouseId = " + warehouseId + ", " +
                "pickerUserId = " + pickerUserId + ", " +
                "status = " + status + ", " +
                "assignedAt = " + assignedAt + ", " +
                "completedAt = " + completedAt + ")";
    }
}
