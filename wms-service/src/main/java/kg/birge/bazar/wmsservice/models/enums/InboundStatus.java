package kg.birge.bazar.wmsservice.models.enums;

public enum InboundStatus {
    PLANNED,                // Запланировано
    IN_TRANSIT,             // В пути
    RECEIVED_PARTIAL,       // Принято частично
    RECEIVED_COMPLETED,     // Принято полностью
    CANCELLED               // Отменено
}
