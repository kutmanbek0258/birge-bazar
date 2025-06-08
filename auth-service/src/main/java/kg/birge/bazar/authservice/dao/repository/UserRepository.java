package kg.birge.bazar.authservice.dao.repository;

import kg.birge.bazar.authservice.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends
    JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {

    UserEntity findByEmail(String email);

    boolean existsByEmail(String email);
}
