package kg.birge.bazar.orderservice.dto;

import kg.birge.bazar.orderservice.model.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {
    private Long id;
    private UUID userId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String shippingAddressJson;
    private List<OrderItemDto> items;
    private ZonedDateTime createdAt;
}
