// domain/InboundShipment.java
package kg.birge.bazar.wmsservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.wmsservice.config.audit.AuditableCustom;
import kg.birge.bazar.wmsservice.models.enums.InboundStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность "Входящая поставка".
 * Управляет процессом приёмки товаров на склад от продавцов или поставщиков.
 */
@Entity
@Table(schema = "wms", name = "inbound_shipments")
@Audited // Включаем аудит для этой сущности
@NoArgsConstructor // Lombok для автоматической генерации конструктора без параметров
@AllArgsConstructor // Lombok для автоматической генерации конструктора со всеми параметрами
@Getter // Lombok для автоматической генерации геттеров
@Setter // Lombok для автоматической генерации сеттеров
// Lombok для автоматической генерации метода toString
public class InboundShipment extends AuditableCustom<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inbound_id")
    private Long inboundId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId; // ID продавца (логический FK к Seller Service)

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId; // ID склада, куда поступает товар (логический FK к WMS.warehouses)

    @Enumerated(EnumType.STRING) // Храним ENUM как строку
    @Column(name = "status", nullable = false)
    private InboundStatus status; // Текущий статус поставки

    @Column(name = "expected_arrival_date", nullable = false)
    private LocalDate expectedArrivalDate; // Ожидаемая дата прибытия

    @Column(name = "actual_arrival_date")
    private LocalDateTime actualArrivalDate; // Фактическая дата и время прибытия

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes; // Дополнительные заметки

    @OneToMany(mappedBy = "inboundShipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InboundItem> items = new ArrayList<>(); // Позиции в этой поставке

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "inboundId = " + inboundId + ", " +
                "createdBy = " + createdBy + ", " +
                "createdDate = " + createdDate + ", " +
                "lastModifiedBy = " + lastModifiedBy + ", " +
                "lastModifiedDate = " + lastModifiedDate + ", " +
                "sellerId = " + sellerId + ", " +
                "warehouseId = " + warehouseId + ", " +
                "status = " + status + ", " +
                "expectedArrivalDate = " + expectedArrivalDate + ", " +
                "actualArrivalDate = " + actualArrivalDate + ", " +
                "notes = " + notes + ")";
    }
}
