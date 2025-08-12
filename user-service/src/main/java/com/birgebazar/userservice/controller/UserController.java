package com.birgebazar.userservice.controller;

import com.birgebazar.userservice.model.User;
import com.birgebazar.userservice.model.UserAddress;
import com.birgebazar.userservice.model.FavoriteProduct;
import com.birgebazar.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Helper to get userId from Principal (OAuth2 token)
    private UUID getUserId(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new IllegalArgumentException("User not authenticated.");
        }
        // Assuming principal.getName() returns the user ID as a String UUID
        return UUID.fromString(principal.getName());
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile(Principal principal) {
        UUID userId = getUserId(principal);
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateMyProfile(Principal principal, @RequestBody User userDetails) {
        UUID userId = getUserId(principal);
        try {
            User updatedUser = userService.updateUser(userId, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/me/addresses")
    public ResponseEntity<List<UserAddress>> getMyAddresses(Principal principal) {
        UUID userId = getUserId(principal);
        List<UserAddress> addresses = userService.getUserAddresses(userId);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/me/addresses")
    public ResponseEntity<UserAddress> addMyAddress(Principal principal, @RequestBody UserAddress address) {
        UUID userId = getUserId(principal);
        try {
            UserAddress newAddress = userService.addUserAddress(userId, address);
            return new ResponseEntity<>(newAddress, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/me/addresses/{addressId}")
    public ResponseEntity<Void> deleteMyAddress(@PathVariable Long addressId) {
        userService.deleteUserAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/favorites")
    public ResponseEntity<List<FavoriteProduct>> getMyFavoriteProducts(Principal principal) {
        UUID userId = getUserId(principal);
        List<FavoriteProduct> favorites = userService.getFavoriteProducts(userId);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/me/favorites")
    public ResponseEntity<FavoriteProduct> addFavoriteProduct(Principal principal, @RequestParam Long productId) {
        UUID userId = getUserId(principal);
        try {
            FavoriteProduct newFavorite = userService.addFavoriteProduct(userId, productId);
            return new ResponseEntity<>(newFavorite, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/me/favorites")
    public ResponseEntity<Void> removeFavoriteProduct(Principal principal, @RequestParam Long productId) {
        UUID userId = getUserId(principal);
        try {
            userService.removeFavoriteProduct(userId, productId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
