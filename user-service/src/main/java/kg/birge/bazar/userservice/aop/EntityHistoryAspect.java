package kg.birge.bazar.userservice.aop;

import cn.hutool.core.date.DateTime;
import kg.birge.bazar.userservice.dto.AddressDto;
import kg.birge.bazar.userservice.dto.SellerProfileDto;
import kg.birge.bazar.userservice.dto.UserProfileDto;
import kg.birge.bazar.userservice.models.EntityHistory;
import kg.birge.bazar.userservice.repository.AddressRepository;
import kg.birge.bazar.userservice.repository.EntityHistoryRepository;
import kg.birge.bazar.userservice.repository.SellerProfileRepository;
import kg.birge.bazar.userservice.repository.UserProfileRepository;
import kg.birge.bazar.userservice.service.AddressService;
import kg.birge.bazar.userservice.service.SellerProfileService;
import kg.birge.bazar.userservice.service.UserProfileService;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

@Aspect
@Component
@AllArgsConstructor
public class EntityHistoryAspect {

    @Autowired
    private final EntityHistoryRepository entityHistoryRepository;
    private final UserProfileRepository userProfileRepository;
    private final SellerProfileRepository sellerProfileRepository;
    private final AddressRepository addressRepository;
    private final UserProfileService userProfileService;
    private final SellerProfileService sellerProfileService;
    private final AddressService addressService;

    @Around("@annotation(kg.birge.bazar.userservice.aop.EntityHistoryAudit)")
    public Object auditEntityChanges(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        EntityHistoryAudit auditAnn = method.getAnnotation(EntityHistoryAudit.class);

        // Получаем аргументы метода
        Object[] args = joinPoint.getArgs();

        // Предполагается, что первым аргументом идет ID сущности, вторым DTO, третьим - changedBy (id кто изменил)
        Long entityId = (Long) args[0];
        Object dto = args[1];
        Long changedBy = (Long) args[2];

        // Достаём старое состояние сущности (например, через сервис или репозиторий)
        Object oldEntity = getOldEntity(auditAnn.entityType(), entityId);

        Object result = joinPoint.proceed(); // выполняем основной метод (например, update)

        // Достаём новое состояние (можно ещё раз получить из БД)
        Object newEntity = getOldEntity(auditAnn.entityType(), entityId);

        // Сравниваем поля и сохраняем EntityHistory для каждого изменённого
        for (var field : auditAnn.dtoClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object oldValue = field.get(oldEntity);
            Object newValue = field.get(newEntity);
            if (!Objects.equals(oldValue, newValue)) {
                EntityHistory history = new EntityHistory();
                history.setEntityType(auditAnn.entityType());
                history.setEntityId(entityId);
                switch (auditAnn.entityType()) {
                    case "USER_PROFILE" -> {
                        UserProfileDto userProfileDto = userProfileService.findById(entityId);
                        history.setOwnerId(userProfileDto.getCreatedBy());
                    }
                    case "SELLER_PROFILE" -> {
                        SellerProfileDto sellerProfileDto = sellerProfileService.findById(entityId);
                        history.setOwnerId(sellerProfileDto.getCreatedBy());
                    }
                    case "ADDRESS" -> {
                        AddressDto addressDto = addressService.findById(entityId);
                        history.setOwnerId(addressDto.getCreatedBy());
                    }
                }
                history.setChangedBy(changedBy);
                history.setFieldName(field.getName());
                history.setOldValue(oldValue != null ? oldValue.toString() : null);
                history.setNewValue(newValue != null ? newValue.toString() : null);
                history.setChangedAt(DateTime.now());
                history.setChangeType(auditAnn.entityChangeType());
                entityHistoryRepository.save(history);
            }
        }

        return result;
    }

    private Object getOldEntity(String entityType, Long entityId) {
        switch (entityType){
            case "USER_PROFILE" -> {
                return userProfileRepository.findById(entityId).orElse(null);
            }
            case "SELLER_PROFILE" -> {
                return sellerProfileRepository.findById(entityId).orElse(null);
            }
            case "ADDRESS" -> {
                return addressRepository.findById(entityId).orElse(null);
            }
            default -> { return null; }

        }
    }
}