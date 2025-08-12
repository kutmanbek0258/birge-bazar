package com.birgebazar.userservice.service;

import com.birgebazar.userservice.model.User;
import com.birgebazar.userservice.model.UserAddress;
import com.birgebazar.userservice.model.FavoriteProduct;
import com.birgebazar.userservice.repository.UserRepository;
import com.birgebazar.userservice.repository.UserAddressRepository;
import com.birgebazar.userservice.repository.FavoriteProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final FavoriteProductRepository favoriteProductRepository;

    @Transactional(readOnly = true)
    public Optional<User> getUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUser(User user) {
        user.setCreatedDate(Instant.now());
        user.setLastModifiedDate(Instant.now());
        // Set createdBy, lastModifiedBy from security context
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UUID userId, User userDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + userId));

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setDateOfBirth(userDetails.getDateOfBirth());
        user.setGender(userDetails.getGender());
        user.setAvatarUrl(userDetails.getAvatarUrl());
        user.setLastModifiedDate(Instant.now());
        // Set lastModifiedBy
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    // --- User Addresses ---

    @Transactional(readOnly = true)
    public List<UserAddress> getUserAddresses(UUID userId) {
        return userAddressRepository.findByUserUserId(userId);
    }

    @Transactional
    public UserAddress addUserAddress(UUID userId, UserAddress address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + userId));
        address.setUser(user);
        address.setCreatedDate(Instant.now());
        address.setLastModifiedDate(Instant.now());
        // Set createdBy, lastModifiedBy
        return userAddressRepository.save(address);
    }

    @Transactional
    public void deleteUserAddress(Long addressId) {
        userAddressRepository.deleteById(addressId);
    }

    // --- Favorite Products ---

    @Transactional(readOnly = true)
    public List<FavoriteProduct> getFavoriteProducts(UUID userId) {
        return favoriteProductRepository.findByUserUserId(userId);
    }

    @Transactional
    public FavoriteProduct addFavoriteProduct(UUID userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + userId));

        if (favoriteProductRepository.findByUserUserIdAndProductId(userId, productId).isPresent()) {
            throw new IllegalArgumentException("Product already in favorites.");
        }

        FavoriteProduct favoriteProduct = new FavoriteProduct();
        favoriteProduct.setUser(user);
        favoriteProduct.setProductId(productId);
        favoriteProduct.setCreatedDate(Instant.now());
        favoriteProduct.setLastModifiedDate(Instant.now());
        // Set createdBy, lastModifiedBy
        return favoriteProductRepository.save(favoriteProduct);
    }

    @Transactional
    public void removeFavoriteProduct(UUID userId, Long productId) {
        FavoriteProduct favoriteProduct = favoriteProductRepository.findByUserUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new IllegalArgumentException("Favorite product not found."));
        favoriteProductRepository.delete(favoriteProduct);
    }
}
