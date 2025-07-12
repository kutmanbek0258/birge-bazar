package kg.birge.bazar.wmsservice.models.enums;

public enum OutboundStatus {
    PENDING_PICKING,    // Ожидает отбора
    READY_FOR_DISPATCH, // Готово к отгрузке
    DISPATCHED,         // Отгружено
    DELIVERED           // Доставлено (может быть обновлено Logistics Service)
}
