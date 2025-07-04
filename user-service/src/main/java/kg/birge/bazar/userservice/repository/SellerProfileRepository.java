package kg.birge.bazar.userservice.repository;

import kg.birge.bazar.userservice.models.SellerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerProfileRepository extends JpaRepository<SellerProfile, Long>,
        JpaSpecificationExecutor<SellerProfile>,
        RevisionRepository<SellerProfile, Long, Long> {
}