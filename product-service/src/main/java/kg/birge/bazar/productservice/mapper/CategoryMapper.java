package kg.birge.bazar.productservice.mapper;

import kg.birge.bazar.productservice.dto.CategoryDto;
import kg.birge.bazar.productservice.models.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDto, Category> {
}