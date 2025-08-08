package kg.birge.bazar.sellerservice.repository;

import kg.birge.bazar.sellerservice.models.Seller;
import kg.birge.bazar.sellerservice.models.SellerStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerStaffRepository extends JpaRepository<SellerStaff, Long> {
    Optional<SellerStaff> findBySellerAndUserId(Seller seller, UUID userId);
}