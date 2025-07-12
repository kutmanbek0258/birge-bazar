// domain/enums/WmsEnums.java
package kg.birge.bazar.wmsservice.models.enums;

/**
 * Перечисления для статусов и типов в WMS.
 */
public enum InventoryStatus {
    AVAILABLE,      // Доступно для отбора/продажи
    RESERVED,       // Зарезервировано под заказ
    QUARANTINE,     // На карантине (например, для проверки качества)
    DAMAGED,        // Повреждено
    IN_TRANSIT      // В процессе перемещения
}