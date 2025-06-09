package kg.birge.bazar.userservice.mapper;

import kg.birge.bazar.userservice.dto.AddressDto;
import kg.birge.bazar.userservice.models.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<AddressDto, Address> {
}