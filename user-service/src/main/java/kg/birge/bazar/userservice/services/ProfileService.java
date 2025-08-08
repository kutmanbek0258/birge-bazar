package kg.birge.bazar.userservice.services;

import kg.birge.bazar.userservice.dto.*;
import java.util.List;
import java.util.UUID;

public interface ProfileService {

    UserDto getMyProfile(UUID userId);

    UserDto updateMyProfile(UUID userId, UpdateUserDto updateUserDto);

    List<UserAddressDto> getMyAddresses(UUID userId);

    UserAddressDto addAddress(UUID userId, CreateAddressDto createAddressDto);

    UserAddressDto updateAddress(UUID userId, Long addressId, UpdateAddressDto updateAddressDto);

    void deleteAddress(UUID userId, Long addressId);

    List<FavoriteProductDto> getMyFavorites(UUID userId);

    FavoriteProductDto addFavorite(UUID userId, AddFavoriteDto addFavoriteDto);

    void removeFavorite(UUID userId, Long favoriteId);
}
