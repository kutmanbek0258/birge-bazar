package com.birgebazar.cartservice.controller;

import com.birgebazar.cartservice.model.Cart;
import com.birgebazar.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Helper to get userId from Principal (OAuth2 token) or use sessionId
    private Long getUserId(Principal principal) {
        // In a real application, you'd parse the JWT to get the user ID
        // For now, assuming principal.getName() returns the user ID as a String
        return (principal != null) ? Long.valueOf(principal.getName()) : null;
    }

    // Helper to get sessionId (e.g., from cookie or header for anonymous users)
    private String getSessionId(@RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        // In a real application, you'd manage session IDs more robustly (e.g., secure cookies)
        return sessionId;
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(Principal principal, @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {
        Long userId = getUserId(principal);
        String currentSessionId = getSessionId(sessionId);

        if (userId == null && currentSessionId == null) {
            return ResponseEntity.badRequest().build(); // Must have either user ID or session ID
        }

        Optional<Cart> cart = cartService.getCartDetails(userId, currentSessionId);
        return cart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addProductToCart(
            Principal principal,
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId,
            @RequestParam Long productId,
            @RequestParam(required = false) Long productVariantId,
            @RequestParam int quantity,
            @RequestParam BigDecimal priceAtAdd) {

        Long userId = getUserId(principal);
        String currentSessionId = getSessionId(sessionId);

        if (userId == null && currentSessionId == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Cart updatedCart = cartService.addProductToCart(userId, currentSessionId, productId, productVariantId, quantity, priceAtAdd);
            return ResponseEntity.ok(updatedCart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Or a more specific error response
        }
    }

    @PutMapping("/update-quantity")
    public ResponseEntity<Cart> updateProductQuantity(
            Principal principal,
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId,
            @RequestParam Long productId,
            @RequestParam(required = false) Long productVariantId,
            @RequestParam int newQuantity) {

        Long userId = getUserId(principal);
        String currentSessionId = getSessionId(sessionId);

        if (userId == null && currentSessionId == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Cart updatedCart = cartService.updateProductQuantity(userId, currentSessionId, productId, productVariantId, newQuantity);
            return ResponseEntity.ok(updatedCart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeProductFromCart(
            Principal principal,
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId,
            @RequestParam Long productId,
            @RequestParam(required = false) Long productVariantId) {

        Long userId = getUserId(principal);
        String currentSessionId = getSessionId(sessionId);

        if (userId == null && currentSessionId == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            cartService.removeProductFromCart(userId, currentSessionId, productId, productVariantId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(
            Principal principal,
            @RequestHeader(value = "X-Session-ID", required = false) String sessionId) {

        Long userId = getUserId(principal);
        String currentSessionId = getSessionId(sessionId);

        if (userId == null && currentSessionId == null) {
            return ResponseEntity.badRequest().build();
        }

        cartService.clearCart(userId, currentSessionId);
        return ResponseEntity.noContent().build();
    }
}
