package kg.birge.bazar.cartservice.repository;

import kg.birge.bazar.cartservice.model.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final RedisTemplate<String, Cart> redisTemplate;
    private static final String KEY_PREFIX = "cart:";

    public Optional<Cart> findById(UUID userId) {
        Cart cart = redisTemplate.opsForValue().get(KEY_PREFIX + userId.toString());
        return Optional.ofNullable(cart);
    }

    public void save(Cart cart) {
        redisTemplate.opsForValue().set(KEY_PREFIX + cart.getUserId().toString(), cart);
    }

    public void deleteById(UUID userId) {
        redisTemplate.delete(KEY_PREFIX + userId.toString());
    }
}
