package kg.birge.bazar.userservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.userservice.config.audit.AuditableCustom;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "favorite_products", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "product_id"})
})
@Getter
@Setter
public class FavoriteProduct extends AuditableCustom<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "product_id", nullable = false)
    private Long productId;
}
