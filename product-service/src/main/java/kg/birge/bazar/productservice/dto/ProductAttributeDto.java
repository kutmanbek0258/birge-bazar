package kg.birge.bazar.productservice.dto;

public class ProductAttributeDto extends AbstractDto<Long> {
    private Long id;
    private Long productId;
    private String attributeName;
    private String attributeValue;

    public ProductAttributeDto() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getAttributeValue() {
        return this.attributeValue;
    }
}