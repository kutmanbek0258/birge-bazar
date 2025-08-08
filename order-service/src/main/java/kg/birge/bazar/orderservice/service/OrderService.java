package kg.birge.bazar.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import kg.birge.bazar.orderservice.client.CartDto;
import kg.birge.bazar.orderservice.client.CartServiceClient;
import kg.birge.bazar.orderservice.dto.CreateOrderRequestDto;
import kg.birge.bazar.orderservice.dto.OrderDto;
import kg.birge.bazar.orderservice.dto.OrderItemDto;
import kg.birge.bazar.orderservice.model.Order;
import kg.birge.bazar.orderservice.model.OrderItem;
import kg.birge.bazar.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartServiceClient cartServiceClient;
    private final ObjectMapper objectMapper; // For converting address to JSON

    @Transactional
    @SneakyThrows
    public OrderDto createOrder(UUID userId, CreateOrderRequestDto request) {
        // 1. Get cart from cart-service
        CartDto cart = cartServiceClient.getMyCart();
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create an order from an empty cart.");
        }

        // In a real app, you would also:
        // 2. Get user's address from user-service by request.getShippingAddressId()
        // 3. Get product details (price, stock) from product-service for each item
        // For MVP, we'll use a placeholder address and assume prices are fixed.

        String placeholderAddressJson = "{\"city\":\"Bishkek\", \"street\":\"Chuy Prospekti\"}";

        Order order = new Order();
        order.setUserId(userId);
        order.setShippingAddressJson(placeholderAddressJson);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (var cartItem : cart.getItems()) {
            // For MVP, we assume price is 100.0 for every item.
            BigDecimal price = new BigDecimal("100.0");
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPricePerUnit(price);
            orderItem.addItem(orderItem);

            totalAmount = totalAmount.add(price.multiply(new BigDecimal(cartItem.getQuantity())));
        }
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // 4. Clear the cart
        cartServiceClient.clearCart();

        // 5. (Future) Trigger payment process

        return toDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getMyOrders(UUID userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long orderId, UUID userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        if (!order.getUserId().equals(userId)) {
            throw new SecurityException("User not authorized to view this order");
        }
        return toDto(order);
    }

    private OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingAddressJson(order.getShippingAddressJson());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setItems(order.getItems().stream().map(this::toItemDto).collect(Collectors.toList()));
        return dto;
    }

    private OrderItemDto toItemDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
        dto.setPricePerUnit(item.getPricePerUnit());
        return dto;
    }
}