package kg.birge.bazar.cartservice.dto;

import kg.birge.bazar.cartservice.model.CartItem;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CartDto {
    private UUID userId;
    private List<CartItem> items;
    // You can add a total price field here, calculated in the service layer.
}
