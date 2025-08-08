package kg.birge.bazar.sellerservice.mapper;

import kg.birge.bazar.sellerservice.dto.SellerStaffDto;
import kg.birge.bazar.sellerservice.models.SellerStaff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SellerStaffMapper {
    @Mapping(target = "role", expression = "java(staff.getRole() != null ? staff.getRole().name() : null)")
    SellerStaffDto toDto(SellerStaff staff);
    List<SellerStaffDto> toDtoList(List<SellerStaff> staffList);
}
