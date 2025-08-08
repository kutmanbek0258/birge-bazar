package kg.birge.bazar.productservice.controller;

import kg.birge.bazar.productservice.dto.CreateProductRequest;
import kg.birge.bazar.productservice.dto.ProductDto;
import kg.birge.bazar.productservice.dto.UpdateProductRequest;
import kg.birge.bazar.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private UUID getUserId(OAuth2AuthenticatedPrincipal principal) {
        String userIdStr = principal.getAttribute("sub");
        if (userIdStr == null) {
            throw new IllegalArgumentException("User ID not found in token");
        }
        return UUID.fromString(userIdStr);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_seller')")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductRequest request,
                                                    @AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal) {
        ProductDto createdProduct = productService.createProduct(request, getUserId(principal));
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_seller')")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,
                                                    @Valid @RequestBody UpdateProductRequest request,
                                                    @AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal) {
        // Here you should also check if the authenticated seller owns the product
        ProductDto updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin')") // Only admins can delete products
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
