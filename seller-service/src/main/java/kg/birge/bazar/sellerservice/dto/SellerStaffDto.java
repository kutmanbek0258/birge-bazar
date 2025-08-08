package kg.birge.bazar.sellerservice.dto;

import lombok.Data;

@Data
public class SellerStaffDto {
    private Long id;
    private Long userId;
    private String role;
    private boolean isActive;
}
