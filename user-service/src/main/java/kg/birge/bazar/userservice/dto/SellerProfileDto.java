package kg.birge.bazar.userservice.dto;

public class SellerProfileDto extends AbstractDto<Long> {
    private Long id;
    private Long userId;
    private String shopName;
    private String description;
    private String legalInfo;
    private String deliveryTerms;
    private String fulfillmentSchema;

    public SellerProfileDto() {
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

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopName() {
        return this.shopName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setLegalInfo(String legalInfo) {
        this.legalInfo = legalInfo;
    }

    public String getLegalInfo() {
        return this.legalInfo;
    }

    public void setDeliveryTerms(String deliveryTerms) {
        this.deliveryTerms = deliveryTerms;
    }

    public String getDeliveryTerms() {
        return this.deliveryTerms;
    }

    public void setFulfillmentSchema(String fulfillmentSchema) {
        this.fulfillmentSchema = fulfillmentSchema;
    }

    public String getFulfillmentSchema() {
        return this.fulfillmentSchema;
    }
}