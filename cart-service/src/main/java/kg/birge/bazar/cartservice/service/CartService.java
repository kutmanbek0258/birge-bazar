package kg.birge.bazar.cartservice.service;

import kg.birge.bazar.cartservice.dto.AddItemRequestDto;
import kg.birge.bazar.cartservice.dto.CartDto;
import kg.birge.bazar.cartservice.model.Cart;
import kg.birge.bazar.cartservice.model.CartItem;
import kg.birge.bazar.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public CartDto getCart(UUID userId) {
        Cart cart = cartRepository.findById(userId).orElse(new Cart(userId));
        return toDto(cart);
    }

    public CartDto addItemToCart(UUID userId, AddItemRequestDto request) {
        Cart cart = cartRepository.findById(userId).orElse(new Cart(userId));
        String productIdStr = String.valueOf(request.getProductId());

        CartItem item = cart.getItems().get(productIdStr);
        if (item != null) {
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            item = new CartItem(request.getProductId(), request.getQuantity());
        }
        cart.getItems().put(productIdStr, item);
        cartRepository.save(cart);
        return toDto(cart);
    }

    public CartDto removeItemFromCart(UUID userId, Long productId) {
        Cart cart = cartRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));
        cart.getItems().remove(String.valueOf(productId));
        cartRepository.save(cart);
        return toDto(cart);
    }

    public void clearCart(UUID userId) {
        cartRepository.deleteById(userId);
    }

    private CartDto toDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setUserId(cart.getUserId());
        dto.setItems(new ArrayList<>(cart.getItems().values()));
        return dto;
    }
}
