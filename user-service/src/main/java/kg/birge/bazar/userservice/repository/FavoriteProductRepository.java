package kg.birge.bazar.userservice.repository;

import kg.birge.bazar.userservice.models.FavoriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {
    List<FavoriteProduct> findByUserId(UUID userId);
    boolean existsByUserIdAndProductId(UUID userId, Long productId);
}

