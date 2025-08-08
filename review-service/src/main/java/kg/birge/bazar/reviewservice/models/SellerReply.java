package kg.birge.bazar.reviewservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.reviewservice.config.audit.AuditableCustom;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Table(schema = "reviews", name = "seller_replies")
@Audited
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SellerReply extends AuditableCustom<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId;

    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "reply_text", nullable = false, columnDefinition = "TEXT")
    private String replyText;
}
