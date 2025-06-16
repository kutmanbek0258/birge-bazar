package kg.birge.bazar.userservice.audit;

import kg.birge.bazar.userservice.models.AuditRevisionEntity;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity customRevisionEntity = (AuditRevisionEntity) revisionEntity;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            customRevisionEntity.setUpdatedBy(authentication.getName());
        } else {
            customRevisionEntity.setUpdatedBy("anonymous"); // или null, или другой дефолт
        }
    }
}