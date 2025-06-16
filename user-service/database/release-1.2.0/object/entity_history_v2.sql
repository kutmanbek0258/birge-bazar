alter table users.entity_history
    drop column old_value;

alter table users.entity_history
    drop column new_value;

alter table users.entity_history
    add old_state text;

alter table users.entity_history
    add new_state text;

COMMENT ON TABLE users.entity_history IS 'Универсальная история изменений для всех сущностей user-service';
COMMENT ON COLUMN users.entity_history.entity_type IS 'Тип сущности (USER_PROFILE, SELLER_PROFILE, ADDRESS, ...)';
COMMENT ON COLUMN users.entity_history.entity_id IS 'ID изменяемой сущности';
COMMENT ON COLUMN users.entity_history.changed_by IS 'ID пользователя, инициировавшего изменение';
COMMENT ON COLUMN users.entity_history.change_type IS 'Тип изменения (UPDATE, CREATE, DELETE)';
COMMENT ON COLUMN users.entity_history.changed_at IS 'Момент изменения';
COMMENT ON COLUMN users.entity_history.old_state IS 'JSON-снимок сущности до изменения';
COMMENT ON COLUMN users.entity_history.new_state IS 'JSON-снимок сущности после изменения';