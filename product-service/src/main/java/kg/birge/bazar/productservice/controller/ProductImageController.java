package kg.birge.bazar.productservice.controller;

import kg.birge.bazar.productservice.dto.ProductImageDto;
import kg.birge.bazar.productservice.service.ProductImageService;
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

@RequestMapping("/product-image")
@RestController
@Slf4j
public class ProductImageController {
    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Validated ProductImageDto productImageDto) {
        productImageService.save(productImageDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductImageDto> findById(@PathVariable("id") Long id) {
        ProductImageDto productImage = productImageService.findById(id);
        return ResponseEntity.ok(productImage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Optional.ofNullable(productImageService.findById(id)).orElseThrow(() -> {
            log.error("Unable to delete non-existent data！");
            return new ResourceNotFoundException("Unable to delete non-existent data! ");
        });
        productImageService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/page-query")
    public ResponseEntity<Page<ProductImageDto>> pageQuery(ProductImageDto productImageDto, @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductImageDto> productImagePage = productImageService.findByCondition(productImageDto, pageable);
        return ResponseEntity.ok(productImagePage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody @Validated ProductImageDto productImageDto, @PathVariable("id") Long id) {
        productImageService.update(productImageDto, id);
        return ResponseEntity.ok().build();
    }
}