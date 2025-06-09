package kg.birge.bazar.userservice.dto;

import kg.birge.bazar.userservice.annotation.CheckEmail;
import kg.birge.bazar.userservice.annotation.CheckMobile;

public class UserProfileDto extends AbstractDto<Long> {
    private Long id;
    private Long userId;
    private String fullName;
    @CheckMobile
    private String phone;
    @CheckEmail
    private String email;
    private String avatarUrl;
    private Long defaultAddressId;

    public UserProfileDto() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setDefaultAddressId(Long defaultAddressId) {
        this.defaultAddressId = defaultAddressId;
    }

    public Long getDefaultAddressId() {
        return this.defaultAddressId;
    }
}