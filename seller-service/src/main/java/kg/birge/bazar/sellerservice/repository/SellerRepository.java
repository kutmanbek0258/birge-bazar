package kg.birge.bazar.sellerservice.repository;

import kg.birge.bazar.sellerservice.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
}