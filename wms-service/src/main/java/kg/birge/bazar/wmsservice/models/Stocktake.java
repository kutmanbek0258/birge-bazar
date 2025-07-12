package kg.birge.bazar.wmsservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.wmsservice.config.audit.AuditableCustom;
import kg.birge.bazar.wmsservice.models.enums.StocktakeStatus;
import kg.birge.bazar.wmsservice.models.enums.StocktakeType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Table(name = "stocktakes", schema = "wms")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Stocktake extends AuditableCustom<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stocktake_id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "initiated_by_user_id", nullable = false)
    private Long initiatedByUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private StocktakeType type; // Тип инвентаризации (например, плановая, внеплановая)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StocktakeStatus status; // Текущий статус инвентаризации

    @OneToMany(mappedBy = "stocktake", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StocktakeItem> items = new java.util.ArrayList<>(); // Позиции, которые нужно проверить в инвентаризации
}