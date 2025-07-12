package kg.birge.bazar.wmsservice.models.enums;

public enum PickingTaskStatus {
    NEW,        // Новое задание
    ASSIGNED,   // Назначено оператору
    IN_PROGRESS,// В процессе выполнения
    COMPLETED,  // Завершено
    CANCELLED   // Отменено
}