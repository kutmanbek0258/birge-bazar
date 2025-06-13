package kg.birge.bazar.userservice.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EntityHistoryAudit {
    /**
     * Тип сущности, например: USER_PROFILE, SELLER_PROFILE, ADDRESS и т.д.
     */
    String entityType();

    /**
     *
     * Тип операции, например: CREATE, UPDATE, DELETE.
     */
    String entityChangeType();

    /**
     * Класс DTO, для сравнения старых и новых значений.
     */
    Class<?> dtoClass();
}