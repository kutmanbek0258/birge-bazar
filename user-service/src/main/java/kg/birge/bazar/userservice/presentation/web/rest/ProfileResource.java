package kg.birge.bazar.userservice.presentation.web.rest;

import kg.birge.bazar.userservice.dto.*;
import kg.birge.bazar.userservice.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileResource {

    private final ProfileService profileService;

    private UUID getUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(profileService.getMyProfile(getUserId(jwt)));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMyProfile(@AuthenticationPrincipal Jwt jwt, @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(profileService.updateMyProfile(getUserId(jwt), updateUserDto));
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<UserAddressDto>> getMyAddresses(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(profileService.getMyAddresses(getUserId(jwt)));
    }

    @PostMapping("/addresses")
    public ResponseEntity<UserAddressDto> addAddress(@AuthenticationPrincipal Jwt jwt, @RequestBody CreateAddressDto createAddressDto) {
        UserAddressDto newAddress = profileService.addAddress(getUserId(jwt), createAddressDto);
        return new ResponseEntity<>(newAddress, HttpStatus.CREATED);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<UserAddressDto> updateAddress(@AuthenticationPrincipal Jwt jwt,
                                                        @PathVariable Long addressId,
                                                        @RequestBody UpdateAddressDto updateAddressDto) {
        return ResponseEntity.ok(profileService.updateAddress(getUserId(jwt), addressId, updateAddressDto));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@AuthenticationPrincipal Jwt jwt, @PathVariable Long addressId) {
        profileService.deleteAddress(getUserId(jwt), addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteProductDto>> getMyFavorites(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(profileService.getMyFavorites(getUserId(jwt)));
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteProductDto> addFavorite(@AuthenticationPrincipal Jwt jwt, @RequestBody AddFavoriteDto addFavoriteDto) {
        FavoriteProductDto newFavorite = profileService.addFavorite(getUserId(jwt), addFavoriteDto);
        return new ResponseEntity<>(newFavorite, HttpStatus.CREATED);
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<Void> removeFavorite(@AuthenticationPrincipal Jwt jwt, @PathVariable Long favoriteId) {
        profileService.removeFavorite(getUserId(jwt), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
