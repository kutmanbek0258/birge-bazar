package kg.birge.bazar.productservice.mapper;

import kg.birge.bazar.productservice.dto.ProductImageDto;
import kg.birge.bazar.productservice.models.ProductImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductImageMapper extends EntityMapper<ProductImageDto, ProductImage> {
}