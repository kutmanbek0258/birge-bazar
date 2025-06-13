CREATE TABLE users.entity_history (
                                      id BIGSERIAL PRIMARY KEY,
                                      entity_type VARCHAR(64) NOT NULL,    -- USER_PROFILE, SELLER_PROFILE, ADDRESS и т.д.
                                      entity_id BIGINT NOT NULL,           -- id сущности (user_profile.id, seller_profile.id и т.д.)
                                      owner_id BIGINT,                     -- id владельца (user_id, seller_id и т.п.), если применимо
                                      changed_by BIGINT,                   -- id пользователя, который изменил
                                      field_name VARCHAR(64) NOT NULL,     -- изменённое поле
                                      old_value VARCHAR(1024),
                                      new_value VARCHAR(1024),
                                      changed_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
                                      change_type VARCHAR(32)             -- UPDATE, CREATE, DELETE

    -- Индексы для быстрого поиска
--                                       INDEX entity_history_entity_type_idx (entity_type),
--                                       INDEX entity_history_entity_id_idx (entity_id)
);

-- Опционально: комментарии
COMMENT ON TABLE users.entity_history IS 'Универсальная история изменений для всех сущностей user-service';
COMMENT ON COLUMN users.entity_history.entity_type IS 'Тип сущности (USER_PROFILE, SELLER_PROFILE, ADDRESS, ...)';
COMMENT ON COLUMN users.entity_history.entity_id IS 'ID изменяемой сущности';
COMMENT ON COLUMN users.entity_history.owner_id IS 'ID владельца (обычно user_id)';
COMMENT ON COLUMN users.entity_history.changed_by IS 'ID пользователя, инициировавшего изменение';
COMMENT ON COLUMN users.entity_history.field_name IS 'Название измененного поля';
COMMENT ON COLUMN users.entity_history.old_value IS 'Значение до изменения';
COMMENT ON COLUMN users.entity_history.new_value IS 'Значение после изменения';
COMMENT ON COLUMN users.entity_history.changed_at IS 'Момент изменения';
COMMENT ON COLUMN users.entity_history.change_type IS 'Тип изменения (UPDATE, CREATE, DELETE)';

-- Пример создания схемы users, если её нет (раскомментировать если нужно)
-- CREATE SCHEMA IF NOT EXISTS users;