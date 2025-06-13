package kg.birge.bazar.userservice.repository;

import kg.birge.bazar.userservice.models.EntityHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityHistoryRepository extends JpaRepository<EntityHistory, Long>, JpaSpecificationExecutor<EntityHistory> {
}