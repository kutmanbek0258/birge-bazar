package kg.birge.bazar.userservice.mapper;

import kg.birge.bazar.userservice.dto.EntityHistoryDto;
import kg.birge.bazar.userservice.models.EntityHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityHistoryMapper extends EntityMapper<EntityHistoryDto, EntityHistory> {
}