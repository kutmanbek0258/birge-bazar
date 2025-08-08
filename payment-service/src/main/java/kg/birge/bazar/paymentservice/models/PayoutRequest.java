package kg.birge.bazar.paymentservice.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payout_request", schema = "payment")
public class PayoutRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID payoutId;

    private Long sellerId;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PayoutStatus status;

    private LocalDateTime processedAt;
    private String transactionId;

    // Getters and Setters
}

enum PayoutStatus {
    PENDING, APPROVED, PAID, REJECTED
}
