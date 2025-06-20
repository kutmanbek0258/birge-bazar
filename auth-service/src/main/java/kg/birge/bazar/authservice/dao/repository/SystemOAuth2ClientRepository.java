package kg.birge.bazar.authservice.dao.repository;

import kg.birge.bazar.authservice.dao.entity.SystemOauth2Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SystemOAuth2ClientRepository
    extends JpaRepository<SystemOauth2Client, String>, JpaSpecificationExecutor<SystemOauth2Client> {

    SystemOauth2Client getByClientId(String clientId);
}
