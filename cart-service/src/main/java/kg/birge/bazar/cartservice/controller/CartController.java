package kg.birge.bazar.cartservice.controller;

import kg.birge.bazar.cartservice.dto.AddItemRequestDto;
import kg.birge.bazar.cartservice.dto.CartDto;
import kg.birge.bazar.cartservice.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private UUID getUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    @GetMapping
    public ResponseEntity<CartDto> getMyCart(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(cartService.getCart(getUserId(jwt)));
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addItem(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody AddItemRequestDto request) {
        return ResponseEntity.ok(cartService.addItemToCart(getUserId(jwt), request));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartDto> removeItem(@AuthenticationPrincipal Jwt jwt, @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(getUserId(jwt), productId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal Jwt jwt) {
        cartService.clearCart(getUserId(jwt));
        return ResponseEntity.noContent().build();
    }
}
