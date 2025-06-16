package kg.birge.bazar.productservice.service;

import cn.hutool.core.bean.BeanUtil;
import jakarta.transaction.Transactional;
import kg.birge.bazar.productservice.dto.ProductDto;
import kg.birge.bazar.productservice.mapper.ProductMapper;
import kg.birge.bazar.productservice.models.Product;
import kg.birge.bazar.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository repository, ProductMapper productMapper) {
        this.repository = repository;
        this.productMapper = productMapper;
    }

    public ProductDto save(ProductDto productDto) {
        Product entity = productMapper.toEntity(productDto);
        return productMapper.toDto(repository.save(entity));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public ProductDto findById(Long id) {
        return productMapper.toDto(repository.findById(id).orElseThrow());
    }

    public Page<ProductDto> findByCondition(ProductDto productDto, Pageable pageable) {
        Page<Product> entityPage = repository.findAll(pageable);
        List<Product> entities = entityPage.getContent();
        return new PageImpl<>(productMapper.toDto(entities), pageable, entityPage.getTotalElements());
    }

    public ProductDto update(ProductDto productDto, Long id) {
        ProductDto data = findById(id);
        Product entity = productMapper.toEntity(productDto);
        BeanUtil.copyProperties(entity, data, "id");
        return save(data);
    }
}