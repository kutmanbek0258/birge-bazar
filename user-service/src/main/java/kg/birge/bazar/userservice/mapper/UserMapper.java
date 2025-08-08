package kg.birge.bazar.userservice.mapper;

import kg.birge.bazar.userservice.dto.UserDto;
import kg.birge.bazar.userservice.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "gender", expression = "java(user.getGender() != null ? user.getGender().name() : null)")
    @Mapping(target = "status", expression = "java(user.getStatus() != null ? user.getStatus().name() : null)")
    UserDto toDto(User user);
}
