package kg.birge.bazar.wmsservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.wmsservice.config.audit.AuditableCustom;
import lombok.*;

@Table(name = "stocktake_items", schema = "wms")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StocktakeItem extends AuditableCustom<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stocktake_item_id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "stocktake_id", nullable = false)
    private Stocktake stocktake;

    @Column(name = "sku_code", nullable = false, length = 100)
    private String skuCode;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "system_quantity", nullable = false)
    private Integer systemQuantity;

    @Column(name = "counted_quantity", nullable = false)
    private Integer countedQuantity;

    @Column(name = "discrepancy", nullable = false)
    private Integer discrepancy;
}