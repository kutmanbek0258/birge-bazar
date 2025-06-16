package kg.birge.bazar.productservice.mapper;

import kg.birge.bazar.productservice.dto.ProductAttributeDto;
import kg.birge.bazar.productservice.models.ProductAttribute;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductAttributeMapper extends EntityMapper<ProductAttributeDto, ProductAttribute> {
}