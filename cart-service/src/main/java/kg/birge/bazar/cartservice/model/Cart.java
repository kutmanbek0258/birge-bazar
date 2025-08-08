package kg.birge.bazar.cartservice.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class Cart implements Serializable {
    private UUID userId;
    private Map<String, CartItem> items = new HashMap<>();

    public Cart(UUID userId) {
        this.userId = userId;
    }
}
