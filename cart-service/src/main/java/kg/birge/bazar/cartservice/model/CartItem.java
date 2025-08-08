package kg.birge.bazar.cartservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {
    private Long productId;
    private Integer quantity;
    // You can add other fields like price, name, imageUrl here
    // to avoid calling product-service every time the cart is displayed.
}
