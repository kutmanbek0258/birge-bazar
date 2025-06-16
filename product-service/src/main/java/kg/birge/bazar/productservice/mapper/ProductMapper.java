package kg.birge.bazar.productservice.mapper;

import kg.birge.bazar.productservice.dto.ProductDto;
import kg.birge.bazar.productservice.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDto, Product> {
}