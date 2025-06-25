package kg.birge.bazar.searchservice.controller;

import kg.birge.bazar.searchservice.service.ProductReindexService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductReindexController {

    private final ProductReindexService productReindexService;

    @PostMapping("/api/search/reindex")
    public ResponseEntity<?> reindexAll() {
        productReindexService.reindexAllProducts();
        return ResponseEntity.ok("Reindexing started");
    }
}