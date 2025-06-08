package kg.birge.bazar.authservice.dao.repository;

import kg.birge.bazar.authservice.dao.entity.ScopeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScopeRepository extends JpaRepository<ScopeEntity, UUID> {

}
