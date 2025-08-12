package com.birgebazar.cartservice.service;

import com.birgebazar.cartservice.model.Cart;
import com.birgebazar.cartservice.model.CartItem;
import com.birgebazar.cartservice.repository.CartRepository;
import com.birgebazar.cartservice.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public Cart getOrCreateCart(Long userId, String sessionId) {
        Optional<Cart> cartOptional = Optional.empty();

        if (userId != null) {
            cartOptional = cartRepository.findByUserId(userId);
        } else if (sessionId != null) {
            cartOptional = cartRepository.findBySessionId(sessionId);
        }

        return cartOptional.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setSessionId(sessionId);
            newCart.setCreatedDate(Instant.now());
            // Set createdBy, lastModifiedBy, lastModifiedDate if you have a security context
            return cartRepository.save(newCart);
        });
    }

    @Transactional
    public Cart addProductToCart(Long userId, String sessionId, Long productId, Long productVariantId, int quantity, BigDecimal priceAtAdd) {
        Cart cart = getOrCreateCart(userId, sessionId);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId) &&
                        (productVariantId == null ? item.getProductVariantId() == null : productVariantId.equals(item.getProductVariantId())))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setLastModifiedDate(Instant.now());
            // Set lastModifiedBy
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductId(productId);
            newItem.setProductVariantId(productVariantId);
            newItem.setQuantity(quantity);
            newItem.setPriceAtAdd(priceAtAdd);
            newItem.setAddedAt(Instant.now());
            newItem.setCreatedDate(Instant.now());
            // Set createdBy, lastModifiedBy, lastModifiedDate
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }
        cart.setLastModifiedDate(Instant.now());
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateProductQuantity(Long userId, String sessionId, Long productId, Long productVariantId, int newQuantity) {
        Cart cart = getOrCreateCart(userId, sessionId);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId) &&
                        (productVariantId == null ? item.getProductVariantId() == null : productVariantId.equals(item.getProductVariantId())))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            if (newQuantity <= 0) {
                cart.getItems().remove(item);
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(newQuantity);
                item.setLastModifiedDate(Instant.now());
                // Set lastModifiedBy
                cartItemRepository.save(item);
            }
            cart.setLastModifiedDate(Instant.now());
            return cartRepository.save(cart);
        } else {
            throw new IllegalArgumentException("Product not found in cart.");
        }
    }

    @Transactional
    public void removeProductFromCart(Long userId, String sessionId, Long productId, Long productVariantId) {
        Cart cart = getOrCreateCart(userId, sessionId);

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId) &&
                        (productVariantId == null ? item.getProductVariantId() == null : productVariantId.equals(item.getProductVariantId())))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
            cart.setLastModifiedDate(Instant.now());
            cartRepository.save(cart);
        } else {
            throw new IllegalArgumentException("Product not found in cart.");
        }
    }

    @Transactional
    public void clearCart(Long userId, String sessionId) {
        Cart cart = getOrCreateCart(userId, sessionId);
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cart.setLastModifiedDate(Instant.now());
        cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
    public Optional<Cart> getCartDetails(Long userId, String sessionId) {
        if (userId != null) {
            return cartRepository.findByUserId(userId);
        } else if (sessionId != null) {
            return cartRepository.findBySessionId(sessionId);
        }
        return Optional.empty();
    }
}
