package kg.birge.bazar.productservice.dto;

public class ProductImageDto extends AbstractDto<Long> {
    private Long id;
    private Long productId;
    private String imageUrl;
    private Boolean isMain;

    public ProductImageDto() {
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }

    public Boolean getIsMain() {
        return this.isMain;
    }
}