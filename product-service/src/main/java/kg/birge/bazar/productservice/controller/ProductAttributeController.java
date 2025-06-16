package kg.birge.bazar.productservice.controller;

import kg.birge.bazar.productservice.dto.ProductAttributeDto;
import kg.birge.bazar.productservice.service.ProductAttributeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/product-attribute")
@RestController
@Slf4j
public class ProductAttributeController {
    private final ProductAttributeService productAttributeService;

    public ProductAttributeController(ProductAttributeService productAttributeService) {
        this.productAttributeService = productAttributeService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Validated ProductAttributeDto productAttributeDto) {
        productAttributeService.save(productAttributeDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductAttributeDto> findById(@PathVariable("id") Long id) {
        ProductAttributeDto productAttribute = productAttributeService.findById(id);
        return ResponseEntity.ok(productAttribute);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Optional.ofNullable(productAttributeService.findById(id)).orElseThrow(() -> {
            log.error("Unable to delete non-existent data！");
            return new ResourceNotFoundException("Unable to delete non-existent data！");
        });
        productAttributeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page-query")
    public ResponseEntity<Page<ProductAttributeDto>> pageQuery(ProductAttributeDto productAttributeDto, @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductAttributeDto> productAttributePage = productAttributeService.findByCondition(productAttributeDto, pageable);
        return ResponseEntity.ok(productAttributePage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody @Validated ProductAttributeDto productAttributeDto, @PathVariable("id") Long id) {
        productAttributeService.update(productAttributeDto, id);
        return ResponseEntity.ok().build();
    }
}