package kg.birge.bazar.userservice.mapper;

import kg.birge.bazar.userservice.dto.UserProfileDto;
import kg.birge.bazar.userservice.models.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfileDto, UserProfile> {
}