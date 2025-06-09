package kg.birge.bazar.userservice.models;

import jakarta.persistence.*;

@Table(name = "user_profiles", schema = "users", indexes = {
        @Index(name = "user_profiles_user_id_key", columnList = "user_id", unique = true)
})
@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone", length = 32)
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "avatar_url", length = 512)
    private String avatarUrl;

    @Column(name = "default_address_id")
    private Long defaultAddressId;

    public Long getDefaultAddressId() {
        return defaultAddressId;
    }

    public void setDefaultAddressId(Long defaultAddressId) {
        this.defaultAddressId = defaultAddressId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}