package kg.birge.bazar.userservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.userservice.config.audit.AuditableCustom;
import org.hibernate.envers.Audited;

@Table(name = "seller_profiles", schema = "users", indexes = {
        @Index(name = "seller_profiles_user_id_key", columnList = "user_id", unique = true)
})
@Entity
@Audited
public class SellerProfile extends AuditableCustom<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "shop_name")
    private String shopName;

    @Lob
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "legal_info")
    private String legalInfo;

    @Lob
    @Column(name = "delivery_terms")
    private String deliveryTerms;

    @Column(name = "fulfillment_schema", length = 8)
    private String fulfillmentSchema;

    public String getFulfillmentSchema() {
        return fulfillmentSchema;
    }

    public void setFulfillmentSchema(String fulfillmentSchema) {
        this.fulfillmentSchema = fulfillmentSchema;
    }

    public String getDeliveryTerms() {
        return deliveryTerms;
    }

    public void setDeliveryTerms(String deliveryTerms) {
        this.deliveryTerms = deliveryTerms;
    }

    public String getLegalInfo() {
        return legalInfo;
    }

    public void setLegalInfo(String legalInfo) {
        this.legalInfo = legalInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}