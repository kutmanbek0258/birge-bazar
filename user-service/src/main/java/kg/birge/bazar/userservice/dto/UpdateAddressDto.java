package kg.birge.bazar.userservice.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateAddressDto {
    private String label;
    private String city;
    private String street;
    private String houseNumber;
    private String apartmentNumber;
    private String zipCode;
    private Boolean isDefault;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
