package kg.birge.bazar.orderservice.controller;

import jakarta.validation.Valid;
import kg.birge.bazar.orderservice.dto.CreateOrderRequestDto;
import kg.birge.bazar.orderservice.dto.OrderDto;
import kg.birge.bazar.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private UUID getUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateOrderRequestDto request) {
        OrderDto createdOrder = orderService.createOrder(getUserId(jwt), request);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getMyOrders(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(orderService.getMyOrders(getUserId(jwt)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id, getUserId(jwt)));
    }
}
