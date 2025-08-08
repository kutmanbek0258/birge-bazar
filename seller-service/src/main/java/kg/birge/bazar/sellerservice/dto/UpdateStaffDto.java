package kg.birge.bazar.sellerservice.dto;

import lombok.Data;

@Data
public class UpdateStaffDto {
    private String role;
    private Boolean isActive;
}
