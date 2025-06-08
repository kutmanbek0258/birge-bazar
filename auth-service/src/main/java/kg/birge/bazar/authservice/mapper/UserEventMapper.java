package kg.birge.bazar.authservice.mapper;

import kg.birge.bazar.authservice.dao.entity.UserEventEntity;
import kg.birge.bazar.authservice.dto.UserEventDto;
import kg.birge.bazar.authservice.service.MessageService;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserEventMapper {

    public UserEventDto map(UserEventEntity entity, MessageService messageService) {
        return UserEventDto.builder()
            .id(entity.getId())
            .eventType(entity.getEventType())
            .eventTypeName(messageService.getMessage(entity.getEventType()))
            .ipAddress(entity.getIpAddress())
            .clientId(entity.getClientId())
            .browser(entity.getBrowser())
            .device(entity.getDevice())
            .os(entity.getOs())
            .creationDate(entity.getCreationDate())
            .build();
    }
}
