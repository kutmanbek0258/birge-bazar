package kg.birge.bazar.userservice.mapper;

import kg.birge.bazar.userservice.dto.FavoriteProductDto;
import kg.birge.bazar.userservice.models.FavoriteProduct;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {
    FavoriteProductDto toDto(FavoriteProduct favorite);
    List<FavoriteProductDto> toDtoList(List<FavoriteProduct> favorites);
}
