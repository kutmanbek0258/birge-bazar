package kg.birge.bazar.sellerservice.dto;

import lombok.Data;

@Data
public class CreateSellerDto {
    private String shopName;
    private String contactEmail;
    private String contactPhone;
}
