package kg.birge.bazar.paymentservice.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_transaction", schema = "payment")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID transactionId;

    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String gatewayTransactionId;

    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethodType;

    private String errorMessage;
    private LocalDateTime processedAt;

    // Getters and Setters
}

enum PaymentStatus {
    PENDING, SUCCESS, FAILED, REFUNDED, AUTHORIZED
}

enum PaymentMethodType {
    CARD, CASH_ON_DELIVERY, E_WALLET
}
