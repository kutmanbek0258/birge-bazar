package kg.birge.bazar.userservice.mapper;

import kg.birge.bazar.userservice.dto.SellerProfileDto;
import kg.birge.bazar.userservice.models.SellerProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SellerProfileMapper extends EntityMapper<SellerProfileDto, SellerProfile> {
}