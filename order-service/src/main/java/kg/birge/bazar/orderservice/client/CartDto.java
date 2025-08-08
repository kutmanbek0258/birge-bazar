package kg.birge.bazar.orderservice.client;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CartDto {
    private UUID userId;
    private List<CartItemDto> items;
}
