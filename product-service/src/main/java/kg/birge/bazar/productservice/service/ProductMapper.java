package kg.birge.bazar.productservice.service;

import kg.birge.bazar.productservice.dto.ProductDto;
import kg.birge.bazar.productservice.dto.ProductImageDto;
import kg.birge.bazar.productservice.dto.ProductVariantDto;
import kg.birge.bazar.productservice.model.Product;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setSellerId(product.getSellerId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setShortDescription(product.getShortDescription());
        dto.setBasePrice(product.getBasePrice());
        dto.setStatus(product.getStatus());
        dto.setMainImageUrl(product.getMainImageUrl());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        if (product.getBrand() != null) {
            dto.setBrandId(product.getBrand().getId());
            dto.setBrandName(product.getBrand().getName());
        }

        dto.setImages(product.getImages() != null ? product.getImages().stream().map(this::toImageDto).collect(Collectors.toList()) : Collections.emptyList());
        dto.setVariants(product.getVariants() != null ? product.getVariants().stream().map(this::toVariantDto).collect(Collectors.toList()) : Collections.emptyList());

        return dto;
    }

    public ProductImageDto toImageDto(kg.birge.bazar.productservice.model.ProductImage image) {
        if (image == null) {
            return null;
        }
        ProductImageDto dto = new ProductImageDto();
        dto.setId(image.getId());
        dto.setUrl(image.getUrl());
        dto.setIsMain(image.getIsMain());
        dto.setOrderIndex(image.getOrderIndex());
        return dto;
    }

    public ProductVariantDto toVariantDto(kg.birge.bazar.productservice.model.ProductVariant variant) {
        if (variant == null) {
            return null;
        }
        ProductVariantDto dto = new ProductVariantDto();
        dto.setId(variant.getId());
        dto.setSkuCode(variant.getSkuCode());
        dto.setAttributesJson(variant.getAttributesJson());
        dto.setPriceModifier(variant.getPriceModifier());
        dto.setStockQuantityFbs(variant.getStockQuantityFbs());
        dto.setImageUrl(variant.getImageUrl());
        dto.setIsActive(variant.getIsActive());
        return dto;
    }
}
