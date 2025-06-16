package kg.birge.bazar.userservice.models;

import jakarta.persistence.*;
import kg.birge.bazar.userservice.config.audit.AuditableCustom;
import org.hibernate.envers.Audited;

@Table(name = "addresses", schema = "users")
@Entity
@Audited
public class Address extends AuditableCustom<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "address_text", length = 1024)
    private String addressText;

    @Column(name = "city", length = 128)
    private String city;

    @Column(name = "postal_code", length = 32)
    private String postalCode;

    @Column(name = "is_default")
    private Boolean isDefault;

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddressText() {
        return addressText;
    }

    public void setAddressText(String addressText) {
        this.addressText = addressText;
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