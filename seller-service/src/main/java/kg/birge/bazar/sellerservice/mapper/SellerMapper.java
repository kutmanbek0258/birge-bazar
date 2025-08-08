package kg.birge.bazar.sellerservice.mapper;

import kg.birge.bazar.sellerservice.dto.SellerDto;
import kg.birge.bazar.sellerservice.models.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    @Mapping(target = "status", expression = "java(seller.getStatus() != null ? seller.getStatus().name() : null)")
    SellerDto toDto(Seller seller);
}
