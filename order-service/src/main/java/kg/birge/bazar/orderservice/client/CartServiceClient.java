package kg.birge.bazar.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cart-service", path = "/api/v1/cart")
public interface CartServiceClient {

    @GetMapping
    CartDto getMyCart();

    @DeleteMapping
    void clearCart();
}
