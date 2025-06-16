package kg.birge.bazar.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import kg.birge.bazar.userservice.audit.AuditRevisionListener;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Getter
@Setter
@Entity
@RevisionEntity
@EntityListeners(AuditRevisionListener.class)
public class AuditRevisionEntity extends DefaultRevisionEntity {
    private String updatedBy;
}
