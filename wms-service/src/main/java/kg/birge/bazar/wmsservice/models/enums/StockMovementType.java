package kg.birge.bazar.wmsservice.models.enums;

public enum StockMovementType {
    RECEIVE,            // Приемка товара на склад
    PICK,               // Отбор товара для заказа
    SHIP,               // Отгрузка товара со склада
    TRANSFER,           // Внутреннее перемещение между ячейками
    ADJUSTMENT_IN,      // Корректировка запасов (увеличение)
    ADJUSTMENT_OUT      // Корректировка запасов (уменьшение)
}
