package kg.birge.bazar.userservice.repository;

import kg.birge.bazar.userservice.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>,
        JpaSpecificationExecutor<UserProfile>,
        RevisionRepository<UserProfile, Long, Long> {
}