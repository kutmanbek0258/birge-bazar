package kg.birge.bazar.sellerservice.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateSellerDto {
    private String shopName;
    private String description;
    private String logoUrl;
    private String contactEmail;
    private String contactPhone;
    private String bankDetailsJson;
}
