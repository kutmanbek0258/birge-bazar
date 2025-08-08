package kg.birge.bazar.sellerservice.services;

import kg.birge.bazar.sellerservice.dto.*;
import java.util.List;
import java.util.UUID;

public interface SellerDashboardService {

    SellerDto createSeller(UUID userId, CreateSellerDto createSellerDto);

    SellerDto getMySellerInfo(UUID userId);

    SellerDto updateMySellerInfo(UUID userId, UpdateSellerDto updateSellerDto);

    List<SellerStaffDto> getMyStaff(UUID userId);

    SellerStaffDto inviteStaff(UUID userId, InviteStaffDto inviteStaffDto);

    SellerStaffDto updateStaff(UUID userId, UUID staffUserId, UpdateStaffDto updateStaffDto);

    void removeStaff(UUID userId, UUID staffUserId);
}
