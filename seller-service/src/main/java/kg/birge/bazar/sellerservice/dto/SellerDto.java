package kg.birge.bazar.sellerservice.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SellerDto {
    private Long id;
    private String shopName;
    private String description;
    private String logoUrl;
    private String status;
    private BigDecimal commissionRate;
    private String contactEmail;
    private String contactPhone;
}
