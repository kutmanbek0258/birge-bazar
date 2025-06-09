package kg.birge.bazar.userservice.dto;

public class AddressDto extends AbstractDto<Long> {
    private Long id;
    private Long userId;
    private String addressText;
    private String city;
    private String postalCode;
    private Boolean isDefault;

    public AddressDto() {
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

    public void setAddressText(String addressText) {
        this.addressText = addressText;
    }

    public String getAddressText() {
        return this.addressText;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsDefault() {
        return this.isDefault;
    }
}