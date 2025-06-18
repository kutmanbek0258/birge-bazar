package kg.birge.bazar.searchservice.controller;

import kg.birge.bazar.searchservice.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/search/products")
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @GetMapping
    public Map<String, Object> searchProducts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable
    ) {
        return productSearchService.searchProducts(query, categoryId, minPrice, maxPrice, pageable);
    }
}