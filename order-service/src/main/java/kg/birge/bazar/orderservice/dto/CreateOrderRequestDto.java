package kg.birge.bazar.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotNull
    private Long shippingAddressId;
}
