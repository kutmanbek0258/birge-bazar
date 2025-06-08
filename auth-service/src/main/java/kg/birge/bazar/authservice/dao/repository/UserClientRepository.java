package kg.birge.bazar.authservice.dao.repository;

import kg.birge.bazar.authservice.dao.entity.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserClientRepository extends JpaRepository<UserClient, String> {

    UserClient findByUserIdAndClientId(UUID userId, String clientId);

    List<UserClient> findByUserId(UUID userId);

    List<UserClient> findAllByDeletedIsTrue();

    void deleteAllByDeletedIsTrue();

}
