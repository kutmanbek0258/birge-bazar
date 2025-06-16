package kg.birge.bazar.productservice.service;

import cn.hutool.core.bean.BeanUtil;
import jakarta.transaction.Transactional;
import kg.birge.bazar.productservice.dto.ProductImageDto;
import kg.birge.bazar.productservice.mapper.ProductImageMapper;
import kg.birge.bazar.productservice.models.ProductImage;
import kg.birge.bazar.productservice.repository.ProductImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
public class ProductImageService {
    private final ProductImageRepository repository;
    private final ProductImageMapper productImageMapper;

    public ProductImageService(ProductImageRepository repository, ProductImageMapper productImageMapper) {
        this.repository = repository;
        this.productImageMapper = productImageMapper;
    }

    public ProductImageDto save(ProductImageDto productImageDto) {
        ProductImage entity = productImageMapper.toEntity(productImageDto);
        return productImageMapper.toDto(repository.save(entity));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public ProductImageDto findById(Long id) {
        return productImageMapper.toDto(repository.findById(id).orElseThrow());
    }

    public Page<ProductImageDto> findByCondition(ProductImageDto productImageDto, Pageable pageable) {
        Page<ProductImage> entityPage = repository.findAll(pageable);
        List<ProductImage> entities = entityPage.getContent();
        return new PageImpl<>(productImageMapper.toDto(entities), pageable, entityPage.getTotalElements());
    }

    public ProductImageDto update(ProductImageDto productImageDto, Long id) {
        ProductImageDto data = findById(id);
        ProductImage entity = productImageMapper.toEntity(productImageDto);
        BeanUtil.copyProperties(entity, data, "id");
        return save(data);
    }
}