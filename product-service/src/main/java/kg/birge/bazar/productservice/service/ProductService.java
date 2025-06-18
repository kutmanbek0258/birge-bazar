package kg.birge.bazar.productservice.service;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kg.birge.bazar.productservice.dto.ProductDto;
import kg.birge.bazar.productservice.mapper.ProductMapper;
import kg.birge.bazar.productservice.models.Product;
import kg.birge.bazar.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;
    @Value("${kafka.topic.product-events:product-events}")
    private String productEventsTopic;
    private String eventType = "CREATED";

    public ProductDto save(ProductDto productDto) {
        Product entity = productMapper.toEntity(productDto);
        Product savedProduct = repository.save(entity);
        publishProductEvent(savedProduct);
        return productMapper.toDto(savedProduct);
    }

    public void deleteById(Long id) {
        Product product = repository.findById(id).orElseThrow();
        repository.delete(product);
        eventType = "DELETED";
        publishProductEvent(product);
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
        eventType = "UPDATED";
        return save(data);
    }

    private void publishProductEvent(Product product) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", eventType);
            event.put("product", product);

            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(productEventsTopic, json);
        } catch (Exception e) {
            // логгируем ошибку, но не прерываем бизнес-логику
        }
    }
}