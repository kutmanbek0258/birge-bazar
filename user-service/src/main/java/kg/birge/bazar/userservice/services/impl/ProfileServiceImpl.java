package kg.birge.bazar.userservice.services.impl;

import jakarta.persistence.EntityNotFoundException;
import kg.birge.bazar.userservice.dto.*;
import kg.birge.bazar.userservice.mapper.AddressMapper;
import kg.birge.bazar.userservice.mapper.FavoriteMapper;
import kg.birge.bazar.userservice.mapper.UserMapper;
import kg.birge.bazar.userservice.models.FavoriteProduct;
import kg.birge.bazar.userservice.models.User;
import kg.birge.bazar.userservice.models.UserAddress;
import kg.birge.bazar.userservice.repository.FavoriteProductRepository;
import kg.birge.bazar.userservice.repository.UserAddressRepository;
import kg.birge.bazar.userservice.repository.UserRepository;
import kg.birge.bazar.userservice.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final FavoriteProductRepository favoriteProductRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final FavoriteMapper favoriteMapper;

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getMyProfile(UUID userId) {
        return userMapper.toDto(findUserById(userId));
    }

    @Override
    @Transactional
    public UserDto updateMyProfile(UUID userId, UpdateUserDto updateUserDto) {
        User user = findUserById(userId);
        userMapper.updateFromDto(updateUserDto, user);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAddressDto> getMyAddresses(UUID userId) {
        User user = findUserById(userId);
        return user.getAddresses().stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserAddressDto addAddress(UUID userId, CreateAddressDto createAddressDto) {
        User user = findUserById(userId);
        UserAddress address = addressMapper.toEntity(createAddressDto);
        address.setUser(user);
        return addressMapper.toDto(userAddressRepository.save(address));
    }

    @Override
    @Transactional
    public UserAddressDto updateAddress(UUID userId, Long addressId, UpdateAddressDto updateAddressDto) {
        UserAddress address = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + addressId));
        if (!address.getUser().getId().equals(userId)) {
            throw new SecurityException("User not authorized to update this address");
        }
        addressMapper.updateFromDto(updateAddressDto, address);
        return addressMapper.toDto(userAddressRepository.save(address));
    }

    @Override
    @Transactional
    public void deleteAddress(UUID userId, Long addressId) {
        UserAddress address = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + addressId));
        if (!address.getUser().getId().equals(userId)) {
            throw new SecurityException("User not authorized to delete this address");
        }
        userAddressRepository.delete(address);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteProductDto> getMyFavorites(UUID userId) {
        return favoriteProductRepository.findByUserId(userId).stream()
                .map(favoriteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FavoriteProductDto addFavorite(UUID userId, AddFavoriteDto addFavoriteDto) {
        User user = findUserById(userId);
        if (favoriteProductRepository.existsByUserIdAndProductId(userId, addFavoriteDto.getProductId())) {
            throw new IllegalStateException("Product is already in favorites.");
        }
        FavoriteProduct favorite = new FavoriteProduct();
        favorite.setUser(user);
        favorite.setProductId(addFavoriteDto.getProductId());
        return favoriteMapper.toDto(favoriteProductRepository.save(favorite));
    }

    @Override
    @Transactional
    public void removeFavorite(UUID userId, Long favoriteId) {
        FavoriteProduct favorite = favoriteProductRepository.findById(favoriteId)
                .orElseThrow(() -> new EntityNotFoundException("Favorite not found with id: " + favoriteId));
        if (!favorite.getUser().getId().equals(userId)) {
            throw new SecurityException("User not authorized to remove this favorite");
        }
        favoriteProductRepository.delete(favorite);
    }
}
