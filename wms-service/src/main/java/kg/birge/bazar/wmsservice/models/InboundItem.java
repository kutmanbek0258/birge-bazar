// domain/InboundItem.java
package kg.birge.bazar.wmsservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.wmsservice.config.audit.AuditableCustom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

/**
 * Сущность "Позиция входящей поставки".
 * Детализирует, какие товары и в каком количестве ожидаются или были приняты
 * в рамках конкретной входящей поставки.
 */
@Entity
@Table(schema = "wms", name = "inbound_items")
@Audited // Включаем аудит для этой сущности
@NoArgsConstructor  // Lombok для автоматической генерации конструктора без параметров
@AllArgsConstructor // Lombok для автоматической генерации конструктора со всеми параметрами
@Getter // Lombok для автоматической генерации геттеров
@Setter // Lombok для автоматической генерации сеттеров
public class InboundItem extends AuditableCustom<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inbound_item_id")
    private Long inboundItemId;

    @ManyToOne(fetch = FetchType.LAZY) // Связь "многие к одному" с InboundShipment
    @JoinColumn(name = "inbound_id", nullable = false)
    private InboundShipment inboundShipment;

    @Column(name = "sku_code", nullable = false, length = 100)
    private String skuCode; // SKU товара

    @Column(name = "expected_quantity", nullable = false)
    private Integer expectedQuantity; // Ожидаемое количество

    @Column(name = "received_quantity", nullable = false)
    private Integer receivedQuantity; // Фактически принятое количество

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "inboundItemId = " + inboundItemId + ", " +
                "createdBy = " + createdBy + ", " +
                "createdDate = " + createdDate + ", " +
                "lastModifiedBy = " + lastModifiedBy + ", " +
                "lastModifiedDate = " + lastModifiedDate + ", " +
                "skuCode = " + skuCode + ", " +
                "expectedQuantity = " + expectedQuantity + ", " +
                "receivedQuantity = " + receivedQuantity + ")";
    }
}
