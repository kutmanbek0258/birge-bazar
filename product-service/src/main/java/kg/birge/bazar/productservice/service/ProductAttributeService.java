package kg.birge.bazar.productservice.service;

import cn.hutool.core.bean.BeanUtil;
import jakarta.transaction.Transactional;
import kg.birge.bazar.productservice.dto.ProductAttributeDto;
import kg.birge.bazar.productservice.mapper.ProductAttributeMapper;
import kg.birge.bazar.productservice.models.ProductAttribute;
import kg.birge.bazar.productservice.repository.ProductAttributeRepository;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ProductAttributeService {
    private final ProductAttributeRepository repository;
    private final ProductAttributeMapper productAttributeMapper;

    public ProductAttributeService(ProductAttributeRepository repository, ProductAttributeMapper productAttributeMapper) {
        this.repository = repository;
        this.productAttributeMapper = productAttributeMapper;
    }

    public ProductAttributeDto save(ProductAttributeDto productAttributeDto) {
        ProductAttribute entity = productAttributeMapper.toEntity(productAttributeDto);
        return productAttributeMapper.toDto(repository.save(entity));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public ProductAttributeDto findById(Long id) {
        return productAttributeMapper.toDto(repository.findById(id).orElseThrow());
    }

    public Page<ProductAttributeDto> findByCondition(ProductAttributeDto productAttributeDto, Pageable pageable) {
        Page<ProductAttribute> entityPage = repository.findAll(pageable);
        List<ProductAttribute> entities = entityPage.getContent();
        return new PageImpl<>(productAttributeMapper.toDto(entities), pageable, entityPage.getTotalElements());
    }

    public ProductAttributeDto update(ProductAttributeDto productAttributeDto, Long id) {
        ProductAttributeDto data = findById(id);
        ProductAttribute entity = productAttributeMapper.toEntity(productAttributeDto);
        BeanUtil.copyProperties(entity, data, "id");
        return save(data);
    }
}