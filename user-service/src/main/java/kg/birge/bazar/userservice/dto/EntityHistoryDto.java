package kg.birge.bazar.userservice.dto;

import cn.hutool.core.date.DateTime;

public class EntityHistoryDto extends AbstractDto<Long> {
    private Long id;
    private String entityType;
    private Long entityId;
    private String ownerId;
    private Long changedBy;
    private String fieldName;
    private String oldValue;
    private String newValue;
    private DateTime changedAt;
    private String changeType;

    public EntityHistoryDto() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getEntityId() {
        return this.entityId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public void setChangedBy(Long changedBy) {
        this.changedBy = changedBy;
    }

    public Long getChangedBy() {
        return this.changedBy;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getOldValue() {
        return this.oldValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getNewValue() {
        return this.newValue;
    }

    public void setChangedAt(DateTime changedAt) {
        this.changedAt = changedAt;
    }

    public DateTime getChangedAt() {
        return this.changedAt;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getChangeType() {
        return this.changeType;
    }
}