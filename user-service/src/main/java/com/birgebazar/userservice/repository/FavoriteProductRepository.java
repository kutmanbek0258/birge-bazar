package com.birgebazar.userservice.repository;

import com.birgebazar.userservice.model.FavoriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {
    List<FavoriteProduct> findByUserUserId(UUID userId);
    Optional<FavoriteProduct> findByUserUserIdAndProductId(UUID userId, Long productId);
}
