package kg.birge.bazar.userservice.mapper;

import kg.birge.bazar.userservice.dto.UserAddressDto;
import kg.birge.bazar.userservice.models.UserAddress;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    UserAddressDto toDto(UserAddress address);
    List<UserAddressDto> toDtoList(List<UserAddress> addresses);
}
