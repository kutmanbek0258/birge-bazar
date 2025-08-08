package kg.birge.bazar.sellerservice.presentation.web.rest;

import kg.birge.bazar.sellerservice.dto.*;
import kg.birge.bazar.sellerservice.services.SellerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seller-dashboard")
@RequiredArgsConstructor
public class SellerDashboardResource {

    private final SellerDashboardService sellerDashboardService;

    private UUID getUserId(OAuth2AuthenticatedPrincipal principal) {
        String userIdStr = principal.getAttribute("sub");
        if (userIdStr == null) {
            throw new IllegalArgumentException("User ID not found in token");
        }
        return UUID.fromString(userIdStr);
    }

    @PostMapping
    public ResponseEntity<SellerDto> createSeller(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal, @RequestBody CreateSellerDto createSellerDto) {
        SellerDto newSeller = sellerDashboardService.createSeller(getUserId(principal), createSellerDto);
        return new ResponseEntity<>(newSeller, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<SellerDto> getMySellerInfo(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal) {
        return ResponseEntity.ok(sellerDashboardService.getMySellerInfo(getUserId(principal)));
    }

    @PutMapping
    public ResponseEntity<SellerDto> updateMySellerInfo(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal, @RequestBody UpdateSellerDto updateSellerDto) {
        return ResponseEntity.ok(sellerDashboardService.updateMySellerInfo(getUserId(principal), updateSellerDto));
    }

    @GetMapping("/staff")
    public ResponseEntity<List<SellerStaffDto>> getMyStaff(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal) {
        return ResponseEntity.ok(sellerDashboardService.getMyStaff(getUserId(principal)));
    }

    @PostMapping("/staff")
    public ResponseEntity<SellerStaffDto> inviteStaff(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal, @RequestBody InviteStaffDto inviteStaffDto) {
        SellerStaffDto newStaff = sellerDashboardService.inviteStaff(getUserId(principal), inviteStaffDto);
        return new ResponseEntity<>(newStaff, HttpStatus.CREATED);
    }

    @PutMapping("/staff/{staffUserId}")
    public ResponseEntity<SellerStaffDto> updateStaff(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal,
                                                      @PathVariable UUID staffUserId,
                                                      @RequestBody UpdateStaffDto updateStaffDto) {
        return ResponseEntity.ok(sellerDashboardService.updateStaff(getUserId(principal), staffUserId, updateStaffDto));
    }

    @DeleteMapping("/staff/{staffUserId}")
    public ResponseEntity<Void> removeStaff(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal, @PathVariable UUID staffUserId) {
        sellerDashboardService.removeStaff(getUserId(principal), staffUserId);
        return ResponseEntity.noContent().build();
    }
}
