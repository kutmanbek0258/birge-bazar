package kg.birge.bazar.paymentservice.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "saved_payment_method", schema = "payment")
public class SavedPaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID methodId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private PaymentMethodType type;

    private String token;
    private String lastFourDigits;
    private String cardBrand;
    private Integer expiryMonth;
    private Integer expiryYear;
    private boolean isDefault;

    // Getters and Setters
}
