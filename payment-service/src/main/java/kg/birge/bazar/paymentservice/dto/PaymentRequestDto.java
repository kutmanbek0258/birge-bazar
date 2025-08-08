package kg.birge.bazar.paymentservice.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequestDto {
    private Long orderId;
    private BigDecimal amount;
}
