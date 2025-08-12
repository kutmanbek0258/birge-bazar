Для преобразования сущностей базы данных в формат PostgreSQL, я предоставлю SQL-скрипты `CREATE TABLE` для каждого микросервиса. Это включает определение типов данных, первичных и уникальных ключей, а также ограничений `NOT NULL`.

Важно помнить, что в микросервисной архитектуре, хотя я и укажу "логические" внешние ключи между сервисами для ясности, физические `FOREIGN KEY` ограничения в базе данных обычно не создаются между таблицами, принадлежащими разным микросервисам. Целостность данных в таких случаях поддерживается на уровне бизнес-логики сервисов и межсервисного взаимодействия (например, с использованием паттерна "Event Sourcing" или "Saga").

Я буду использовать `BIGINT` для большинства ID (для обеспечения высокой масштабируемости, хотя `UUID` также является хорошим выбором для глобальной уникальности), `TIMESTAMP WITH TIME ZONE` для временных меток и `NUMERIC` для денежных значений.

### **Аудиторские поля**
Большинство таблиц будут включать следующие поля для аудита, соответствующие `AuditableCustom`:

```sql
created_by VARCHAR(255),
created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
last_modified_by VARCHAR(255),
last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
```

-----

### **Сущности Баз Данных в Формате PostgreSQL**

-----

### **Соответствие Микросервисов и Баз Данных**

Ниже представлено соответствие каждого микросервиса его выделенной базе данных. Это обеспечивает изоляцию данных и независимость развертывания.

*   **User Service:** `user_db`
*   **Auth Service:** `auth_db`
*   **Product Service:** `product_db`
*   **Order Service:** `order_db`
*   **Review Service:** `review_db`
*   **Payment Service:** `payment_db`
*   **Notification Service:** `notification_db`
*   **Logistics Service:** `logistics_db`
*   **WMS Service:** `wms_db`
*   **Admin Service:** `admin_db`
*   **Seller Service:** `seller_db`
*   **Promotion Service:** `promotion_db`
*   **QA Service:** `qa_db`
*   **Messaging Service:** `messaging_db`
*   **Search Service:** `search_db`
*   **Cart Service:** `cart_db`

-----

#### **Общие ENUM типы (если используются несколькими сервисами)**

В реальной системе ENUM'ы часто определяются в конкретных сервисах, но для демонстрации можно представить общие.

```sql
-- Общие ENUM типы (могут быть определены в отдельных сервисах)
CREATE TYPE user_service.user_status_enum AS ENUM ('ACTIVE', 'BLOCKED', 'PENDING_VERIFICATION');
CREATE TYPE user_service.gender_enum AS ENUM ('MALE', 'FEMALE', 'OTHER');
CREATE TYPE order_service.order_status_enum AS ENUM ('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'REFUNDED');
CREATE TYPE payment_service.payment_status_enum AS ENUM ('PENDING', 'PAID', 'FAILED', 'REFUNDED', 'AUTHORIZED');
CREATE TYPE payment_service.payment_method_type_enum AS ENUM ('CARD', 'CASH_ON_DELIVERY', 'E_WALLET');
CREATE TYPE review_service.review_status_enum AS ENUM ('PENDING_MODERATION', 'APPROVED', 'REJECTED', 'HIDDEN');
CREATE TYPE notification_service.notification_type_enum AS ENUM ('ORDER_STATUS', 'PROMOTION', 'MESSAGE', 'REVIEW_REMINDER', 'WMS_ALERT');
CREATE TYPE logistics_service.shipping_method_type_enum AS ENUM ('COURIER', 'PICKUP', 'POSTAMAT');

CREATE TYPE wms_service.warehouse_location_type_enum AS ENUM ('SHELF', 'PALLET', 'FLOOR', 'BIN');
CREATE TYPE wms_service.inventory_item_status_enum AS ENUM ('AVAILABLE', 'RESERVED', 'QUARANTINE', 'DAMAGED');
CREATE TYPE wms_service.stock_movement_type_enum AS ENUM ('RECEIVE', 'PICK', 'SHIP', 'TRANSFER', 'ADJUSTMENT_IN', 'ADJUSTMENT_OUT');
CREATE TYPE wms_service.inbound_status_enum AS ENUM ('PLANNED', 'IN_TRANSIT', 'RECEIVED_PARTIAL', 'RECEIVED_COMPLETED', 'CANCELLED');
CREATE TYPE wms_service.picking_task_status_enum AS ENUM ('NEW', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');
CREATE TYPE wms_service.outbound_status_enum AS ENUM ('PENDING_PICKING', 'READY_FOR_DISPATCH', 'DISPATCHED', 'DELIVERED');
CREATE TYPE wms_service.stocktake_status_enum AS ENUM ('PLANNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');
CREATE TYPE wms_service.stocktake_type_enum AS ENUM ('FULL', 'CYCLE_COUNT');

CREATE TYPE admin_service.moderation_item_type_enum AS ENUM ('PRODUCT', 'REVIEW', 'SELLER', 'USER', 'COMPLAINT');
CREATE TYPE admin_service.moderation_status_enum AS ENUM ('PENDING', 'IN_REVIEW', 'APPROVED', 'REJECTED', 'ACTION_TAKEN');
CREATE TYPE admin_service.platform_setting_value_type_enum AS ENUM ('STRING', 'NUMBER', 'BOOLEAN', 'JSON');
CREATE TYPE seller_service.seller_status_enum AS ENUM ('PENDING_APPROVAL', 'ACTIVE', 'BLOCKED', 'SUSPENDED');
CREATE TYPE seller_service.seller_staff_role_enum AS ENUM ('MANAGER', 'PRODUCT_EDITOR', 'ORDER_PROCESSOR', 'FINANCIER', 'MESSENGER');
CREATE TYPE promotion_service.promotion_discount_type_enum AS ENUM ('PERCENTAGE', 'FIXED_AMOUNT', 'FREE_SHIPPING');
CREATE TYPE qa_service.qa_question_status_enum AS ENUM ('PENDING_ANSWER', 'ANSWERED', 'HIDDEN');
CREATE TYPE qa_service.qa_responder_type_enum AS ENUM ('USER', 'SELLER', 'ADMIN');
CREATE TYPE messaging_service.chat_participant_type_enum AS ENUM ('USER', 'SELLER', 'ADMIN');
CREATE TYPE product_service.product_status_enum AS ENUM ('ACTIVE', 'DRAFT', 'PENDING_MODERATION', 'REJECTED', 'ARCHIVED');
CREATE TYPE auth_service.auth_two_factor_method_enum AS ENUM ('TOTP', 'SMS', 'EMAIL');
CREATE TYPE auth_service.auth_login_status_enum AS ENUM ('SUCCESS', 'FAILED');
```

-----

#### **1. User Service (База данных: `user_db`)**

```sql
-- Таблица для пользователей
CREATE TABLE user_service.users (
    user_id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(50) UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    gender user_service.gender_enum,
    avatar_url TEXT,
    status user_service.user_status_enum NOT NULL DEFAULT 'PENDING_VERIFICATION',
    last_activity_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для адресов пользователей
CREATE TABLE user_service.user_addresses (
    address_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    label VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    street VARCHAR(255) NOT NULL,
    house_number VARCHAR(50) NOT NULL,
    apartment_number VARCHAR(50),
    zip_code VARCHAR(20),
    is_default BOOLEAN DEFAULT FALSE,
    latitude NUMERIC(10, 7),
    longitude NUMERIC(10, 7),
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_service.users(user_id) ON DELETE CASCADE
);

-- Таблица для избранных товаров
CREATE TABLE user_service.favorite_products (
    favorite_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL, -- Logical FK to product_db.products(product_id)
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, product_id), -- Пользователь может добавить один товар в избранное только один раз
    FOREIGN KEY (user_id) REFERENCES user_service.users(user_id) ON DELETE CASCADE
);
```

-----

#### **2. Auth Service (База данных: `auth_db`)**

```sql
-- Схема для сервиса аутентификации
CREATE SCHEMA IF NOT EXISTS sso;

-- Таблица для пользователей
CREATE TABLE sso.users
(
    user_id               UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    email                 VARCHAR(100)                NOT NULL,
    password_hash         VARCHAR(500),
    first_name            varchar(100)                NOT NULL,
    last_name             varchar(100)                NOT NULL,
    middle_name           varchar(100),
    birthday              date,
    active                boolean                     not null default false,

    created_by            VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_updated_by       VARCHAR(50)                 NOT NULL DEFAULT 'system',
    last_updated_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    object_version_number INTEGER                     NOT NULL DEFAULT 0,
    constraint users_pk PRIMARY KEY (user_id)
);

COMMENT ON TABLE sso.users IS 'Пользователи';
COMMENT ON COLUMN sso.users.user_id IS 'УИ пользователя';
COMMENT ON COLUMN sso.users.email IS 'Логин пользователя';
COMMENT ON COLUMN sso.users.password_hash IS 'Хэш пароля';
COMMENT ON COLUMN sso.users.first_name IS 'Имя';
COMMENT ON COLUMN sso.users.last_name IS 'Фамилия';
COMMENT ON COLUMN sso.users.middle_name IS 'Отчество';
COMMENT ON COLUMN sso.users.birthday IS 'Дата рождения';
COMMENT ON COLUMN sso.users.active IS 'Статус пользователя, активен или неактивен';

COMMENT ON column sso.users.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.users.created_date IS 'Дата создания записи';
COMMENT ON column sso.users.last_updated_by IS 'Логин пользователя, изменившего запись';
COMMENT ON column sso.users.last_updated_date IS 'Дата последнего обновления записи';
COMMENT ON column sso.users.object_version_number IS 'Номер версии записи в БД';

-- Changeset users-2: Add unique index on email
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_u1 ON sso.users (email);

-- Changeset users-3: Add avatar_file_id
ALTER TABLE sso.users
    ADD COLUMN avatar_file_id uuid;
COMMENT ON column sso.users.avatar_file_id IS 'ID файла в файловом хранилище для аватарки';

-- Changeset users-4: Drop avatar_url
ALTER TABLE sso.users
    DROP COLUMN avatar_url;

-- Changeset users-5: Add admin column
ALTER TABLE sso.users
    ADD COLUMN admin BOOLEAN NOT NULL DEFAULT FALSE;
COMMENT ON column sso.users.admin IS 'Является ли пользователь админом';

-- Changeset users-7: Add superuser column
ALTER TABLE sso.users
    ADD COLUMN superuser BOOLEAN NOT NULL DEFAULT FALSE;
COMMENT ON column sso.users.superuser IS 'Является ли пользователь суперпользователем (нельзя удалить)';


-- Таблица для OAuth2 клиентов системы (старая версия)
CREATE SEQUENCE sso.system_oauth2_clients_sq START 1;
CREATE TABLE sso.system_oauth2_clients
(
    system_client_id              BIGINT        NOT NULL DEFAULT nextval('sso.system_oauth2_clients_sq'),
    client_id                     VARCHAR(100)  NOT NULL,
    client_id_issued_at           TIMESTAMP              DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret                 VARCHAR(200),
    client_secret_expires_at      TIMESTAMP,
    client_name                   VARCHAR(200)  NOT NULL,
    client_authentication_methods VARCHAR(1000) NOT NULL,
    authorization_grant_types     VARCHAR(1000) NOT NULL,
    redirect_uris                 VARCHAR(1000),
    scopes                        VARCHAR(1000) NOT NULL,
    client_settings               VARCHAR(2000),
    token_settings                VARCHAR(2000),
    constraint system_oauth2_clients_pk primary key (system_client_id)
);

COMMENT ON table sso.system_oauth2_clients IS 'OAuth2 клиенты системы';
COMMENT ON column sso.system_oauth2_clients.client_id IS 'ID клиента';
COMMENT ON column sso.system_oauth2_clients.client_id_issued_at IS 'Дата создания записи';
COMMENT ON column sso.system_oauth2_clients.client_secret IS 'Пароль';
COMMENT ON column sso.system_oauth2_clients.client_secret_expires_at IS 'Срок действия пароля';
COMMENT ON column sso.system_oauth2_clients.client_name IS 'Наименование клиента';
COMMENT ON column sso.system_oauth2_clients.client_authentication_methods IS 'Доступные методы аутентификации';
COMMENT ON column sso.system_oauth2_clients.authorization_grant_types IS 'Типы доступа';
COMMENT ON column sso.system_oauth2_clients.redirect_uris IS 'Доступные URL-ы перенаправления';
COMMENT ON column sso.system_oauth2_clients.scopes IS 'Области доступа';
COMMENT ON column sso.system_oauth2_clients.client_settings IS 'Дополнительные настройки клиента';
COMMENT ON column sso.system_oauth2_clients.token_settings IS 'Дополнительные настройки токена';
CREATE UNIQUE INDEX idx_system_oauth2_clients_n1 ON sso.system_oauth2_clients (client_id);


-- Таблица для справочника привилегий
CREATE TABLE sso.authorities
(
    authority_id          UUID                        NOT NULL default uuid_generate_v4(),
    authority_code        VARCHAR(100)                NOT NULL,
    authority_description VARCHAR(500)                NOT NULL,
    system_code           VARCHAR(50)                 NOT NULL,
    active                boolean                     not null default true,

    created_by            VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_updated_by       VARCHAR(50)                 NOT NULL DEFAULT 'system',
    last_updated_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    object_version_number INTEGER                     NOT NULL DEFAULT 0,
    constraint authorities_pk PRIMARY KEY (authority_id)
);
COMMENT ON TABLE sso.authorities IS 'Справочник привилегий';
COMMENT ON COLUMN sso.authorities.authority_id IS 'Уникальный идентификатор привилегии';
COMMENT ON COLUMN sso.authorities.authority_code IS 'Код привилегии';
COMMENT ON COLUMN sso.authorities.authority_description IS 'Описание привилегии';
COMMENT ON COLUMN sso.authorities.system_code IS 'Код системы, к которой принадлежит привилегия';
COMMENT ON COLUMN sso.authorities.active IS 'Флаг активности';
COMMENT ON column sso.authorities.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.authorities.created_date IS 'Дата создания записи';
COMMENT ON column sso.authorities.last_updated_by IS 'Логин пользователя, изменившего запись';
COMMENT ON column sso.authorities.last_updated_date IS 'Дата последнего обновления записи';
COMMENT ON column sso.authorities.object_version_number IS 'Номер версии записи в БД';
CREATE UNIQUE INDEX idx_authorities_u1 ON sso.authorities (authority_code, system_code);


-- Таблица для справочника ролей
CREATE TABLE sso.roles
(
    role_id               UUID                        NOT NULL default uuid_generate_v4(),
    role_code             VARCHAR(50)                 NOT NULL,
    role_description      VARCHAR(500)                NOT NULL,
    system_code           VARCHAR(50),
    active                boolean                     not null default true,

    created_by            VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_updated_by       VARCHAR(50)                 NOT NULL DEFAULT 'system',
    last_updated_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    object_version_number INTEGER                     NOT NULL DEFAULT 0,
    constraint roles_pk PRIMARY KEY (role_id) -- Changed from role_code to role_id
);
COMMENT ON TABLE sso.roles IS 'Справочник ролей';
COMMENT ON COLUMN sso.roles.role_id IS 'Уникальный идентификатор роли';
COMMENT ON COLUMN sso.roles.role_code IS 'Код роли';
COMMENT ON COLUMN sso.roles.role_description IS 'Описание роли';
COMMENT ON COLUMN sso.roles.system_code IS 'Код системы, к которой принадлежит привилегия';
COMMENT ON COLUMN sso.roles.active IS 'Флаг активности';
COMMENT ON column sso.roles.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.roles.created_date IS 'Дата создания записи';
COMMENT ON column sso.roles.last_updated_by IS 'Логин пользователя, изменившего запись';
COMMENT ON column sso.roles.last_updated_date IS 'Дата последнего обновления записи';
COMMENT ON column sso.roles.object_version_number IS 'Номер версии записи в БД';
CREATE UNIQUE INDEX idx_roles_u1 ON sso.roles (role_code, system_code);


-- Таблица для маппинга ролей и привилегий
CREATE TABLE sso.role_authorities
(
    role_authority_id UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    role_id           UUID                        NOT NULL,
    authority_id      UUID                        NOT NULL,

    created_by        VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    constraint role_authorities_pk PRIMARY KEY (role_authority_id)
);
COMMENT ON TABLE sso.role_authorities IS 'Маппинг ролей и привилегий';
COMMENT ON COLUMN sso.role_authorities.role_authority_id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN sso.role_authorities.role_id IS 'Уникальный код привилегии'; -- Should be role_id
COMMENT ON COLUMN sso.role_authorities.authority_id IS 'Уникальный код привилегии';
COMMENT ON column sso.role_authorities.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.role_authorities.created_date IS 'Дата создания записи';
CREATE UNIQUE INDEX idx_role_authorities_u1 ON sso.role_authorities (role_id, authority_id);


-- Таблица для маппинга пользователей и ролей
CREATE TABLE sso.user_roles
(
    user_role_id UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    user_id      UUID                        NOT NULL,
    role_id      UUID                        NOT NULL,

    created_by   VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    constraint user_roles_pk PRIMARY KEY (user_role_id)
);
COMMENT ON TABLE sso.user_roles IS 'Маппинг пользователей и ролей';
COMMENT ON COLUMN sso.user_roles.user_role_id IS 'УИ записи';
COMMENT ON COLUMN sso.user_roles.user_id IS 'Уникальный идентификатор пользователя';
COMMENT ON COLUMN sso.user_roles.role_id IS 'Уникальный идентификатор роли';
COMMENT ON column sso.user_roles.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.user_roles.created_date IS 'Дата создания записи';
CREATE UNIQUE INDEX idx_user_roles_u1 ON sso.user_roles (user_id, role_id);


-- Таблица для хранилища файлов
CREATE TABLE sso.file_storage
(
    file_id               uuid                        not null default uuid_generate_v4(),
    store_type            varchar(50)                 not null,
    filename              varchar(255)                not null,
    file_size             bigint                      not null,
    content_type          varchar(100)                not null,
    bucket                varchar(50)                 not null,

    created_by            VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_updated_by       VARCHAR(50)                 NOT NULL DEFAULT 'system',
    last_updated_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    object_version_number INTEGER                     NOT NULL DEFAULT 0,
    constraint file_storage_pk PRIMARY KEY (file_id)
);
COMMENT ON table sso.file_storage IS 'Хранилище файлов';
COMMENT ON column sso.file_storage.file_id IS 'Уникальный ID записи';
COMMENT ON column sso.file_storage.store_type IS 'Тип загруженного файла';
COMMENT ON column sso.file_storage.filename IS 'Наименование файла';
COMMENT ON column sso.file_storage.file_size IS 'Размер файла (байт)';
COMMENT ON column sso.file_storage.content_type IS 'MIME Type';
COMMENT ON column sso.file_storage.bucket IS 'Директория хранения';
COMMENT ON column sso.file_storage.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.file_storage.created_date IS 'Дата создания записи';
COMMENT ON column sso.file_storage.last_updated_by IS 'Логин пользователя, изменившего запись';
COMMENT ON column sso.file_storage.last_updated_date IS 'Дата последнего обновления записи';
COMMENT ON column sso.file_storage.object_version_number IS 'Номер версии записи в БД';


-- Таблица для OAuth2 клиентов системы (версия 2)
CREATE TABLE sso.system_oauth2_clients_v2
(
    client_id                     VARCHAR(100)                NOT NULL,
    client_secret                 VARCHAR(255),
    client_secret_expires_at      TIMESTAMP,
    client_name                   VARCHAR(200)                NOT NULL,
    client_authentication_methods VARCHAR(20)[]               NOT NULL,
    authorization_grant_types     VARCHAR(20)[]               NOT NULL,
    redirect_uris                 VARCHAR(255)[],
    scopes                        VARCHAR(100)[]              NOT NULL,
    delete_notify_uris            VARCHAR(255)[],

    created_by                    VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date                  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_updated_by               VARCHAR(50)                 NOT NULL DEFAULT 'system',
    last_updated_date             TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    object_version_number         INTEGER                     NOT NULL DEFAULT 0,
    constraint system_oauth2_clients_v2_pk primary key (client_id)
);
COMMENT ON table sso.system_oauth2_clients_v2 IS 'OAuth2 клиенты системы';
COMMENT ON column sso.system_oauth2_clients_v2.client_id IS 'ID клиента';
COMMENT ON column sso.system_oauth2_clients_v2.client_secret IS 'Пароль';
COMMENT ON column sso.system_oauth2_clients_v2.client_secret_expires_at IS 'Срок действия пароля';
COMMENT ON column sso.system_oauth2_clients_v2.client_name IS 'Наименование клиента';
COMMENT ON column sso.system_oauth2_clients_v2.client_authentication_methods IS 'Доступные методы аутентификации';
COMMENT ON column sso.system_oauth2_clients_v2.authorization_grant_types IS 'Типы доступа';
COMMENT ON column sso.system_oauth2_clients_v2.redirect_uris IS 'Доступные URL-ы перенаправления';
COMMENT ON column sso.system_oauth2_clients_v2.scopes IS 'Области доступа';
COMMENT ON column sso.system_oauth2_clients_v2.delete_notify_uris IS 'Доступные URL-ы уведомления об удалении аккаунта';
COMMENT ON column sso.system_oauth2_clients_v2.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.system_oauth2_clients_v2.created_date IS 'Дата создания записи';
COMMENT ON column sso.system_oauth2_clients_v2.last_updated_by IS 'Логин пользователя, изменившего запись';
COMMENT ON column sso.system_oauth2_clients_v2.last_updated_date IS 'Дата последнего обновления записи';
COMMENT ON column sso.system_oauth2_clients_v2.object_version_number IS 'Номер версии записи в БД';


-- Таблица для событий пользователей
CREATE TABLE sso.user_events
(
    event_id      UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    event_type    VARCHAR(100)                NOT NULL,
    user_agent    VARCHAR(500)                NOT NULL,
    ip_address    varchar(50),
    client_id     varchar(50),
    agent_browser varchar(50),
    agent_device  varchar(50),
    agent_os      varchar(50),

    created_by    VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    constraint user_events_pk PRIMARY KEY (event_id)
);
COMMENT ON TABLE sso.user_events IS 'События пользователей';
COMMENT ON COLUMN sso.user_events.event_id IS 'УИ пользователя';
COMMENT ON COLUMN sso.user_events.event_type IS 'Логин пользователя';
COMMENT ON COLUMN sso.user_events.user_agent IS 'Значение заголовка запроса User-Agent';
COMMENT ON COLUMN sso.user_events.ip_address IS 'IP адрес пользователя';
COMMENT ON COLUMN sso.user_events.client_id IS 'Уникальный идентификатор клиента';
COMMENT ON COLUMN sso.user_events.agent_browser IS 'Браузер пользователя';
COMMENT ON COLUMN sso.user_events.agent_device IS 'Информация об устройстве пользователя';
COMMENT ON COLUMN sso.user_events.agent_os IS 'Информация об OS пользователя';
COMMENT ON column sso.user_events.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.user_events.created_date IS 'Дата создания записи';
CREATE INDEX idx_user_events_n1 ON sso.user_events (date(created_date));
CREATE INDEX idx_user_events_n2 ON sso.user_events (created_by);


-- Таблица для связи пользователя и клиента системы
CREATE TABLE sso.user_clients
(
    user_client_id        UUID                        NOT NULL DEFAULT uuid_generate_v4(),
    user_id               UUID                        NOT NULL,
    client_id             VARCHAR(100)                not null,
    deleted               boolean                     not null default false,

    created_by            VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_updated_by       VARCHAR(50)                 NOT NULL DEFAULT 'system',
    last_updated_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    object_version_number INTEGER                     NOT NULL DEFAULT 0,
    constraint user_client_pk PRIMARY KEY (user_client_id)
);
COMMENT ON TABLE sso.user_clients IS 'Связь пользователя и клиента системы для которого проходил аутентификацию';
COMMENT ON COLUMN sso.user_clients.user_client_id IS 'УИ записи';
COMMENT ON COLUMN sso.user_clients.user_id IS 'УИ пользователя';
COMMENT ON COLUMN sso.user_clients.client_id IS 'УИ клиента';
COMMENT ON COLUMN sso.user_clients.deleted IS 'Запись помечена на удаление';
COMMENT ON column sso.user_clients.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.user_clients.created_date IS 'Дата создания записи';
COMMENT ON column sso.user_clients.last_updated_by IS 'Логин пользователя, изменившего запись';
COMMENT ON column sso.user_clients.last_updated_date IS 'Дата последнего обновления записи';
COMMENT ON column sso.user_clients.object_version_number IS 'Номер версии записи в БД';
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_u1 ON sso.user_clients (user_id, client_id);


-- Таблица для областей доступа (scopes)
CREATE TABLE sso.scopes
(
    scope_id              UUID                        NOT NULL default uuid_generate_v4(),
    scope_code            VARCHAR(100)                NOT NULL,
    scope_description     VARCHAR(500)                NOT NULL,
    system_code           VARCHAR(50)                 NOT NULL,
    active                boolean                     not null default true,

    created_by            VARCHAR(50)                 NOT NULL DEFAULT 'system',
    created_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    last_updated_by       VARCHAR(50)                 NOT NULL DEFAULT 'system',
    last_updated_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    object_version_number INTEGER                     NOT NULL DEFAULT 0,
    constraint scopes_pk PRIMARY KEY (scope_id)
);
COMMENT ON TABLE sso.scopes IS 'Справочник привилегий';
COMMENT ON COLUMN sso.scopes.scope_id IS 'Уникальный идентификатор scope';
COMMENT ON COLUMN sso.scopes.scope_code IS 'Код scope';
COMMENT ON COLUMN sso.scopes.scope_description IS 'Описание scope';
COMMENT ON COLUMN sso.scopes.system_code IS 'Код системы, к которой принадлежит scope';
COMMENT ON COLUMN sso.scopes.active IS 'Флаг активности';
COMMENT ON column sso.scopes.created_by IS 'Логин пользователя, создавшего запись';
COMMENT ON column sso.scopes.created_date IS 'Дата создания записи';
COMMENT ON column sso.scopes.last_updated_by IS 'Логин пользователя, изменившего запись';
COMMENT ON column sso.scopes.last_updated_date IS 'Дата последнего обновления записи';
COMMENT ON column sso.scopes.object_version_number IS 'Номер версии записи в БД';
CREATE UNIQUE INDEX idx_scopes_u1 ON sso.scopes (scope_code, system_code);


-- Представление для областей доступа (scopes)
CREATE OR REPLACE VIEW sso.scopes_vw AS
(
SELECT scope_id,
       system_code || '.' || scope_code AS scope_unique_code,
       scope_description,
       system_code,
       active
FROM sso.scopes
);
```

-----

#### **3. Product Service (База данных: `product_db`)**

```sql
-- Таблица для категорий товаров
CREATE TABLE product_service.categories (
    category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT,
    slug VARCHAR(255) UNIQUE NOT NULL,
    icon_url TEXT,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES product_service.categories(category_id) ON DELETE SET NULL
);

-- Таблица для брендов
CREATE TABLE product_service.brands (
    brand_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    logo_url TEXT,
    description TEXT,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для товаров
CREATE TABLE product_service.products (
    product_id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    short_description VARCHAR(500),
    base_price NUMERIC(10, 2) NOT NULL CHECK (base_price >= 0),
    category_id BIGINT NOT NULL,
    brand_id BIGINT,
    status product_service.product_status_enum NOT NULL DEFAULT 'DRAFT',
    main_image_url TEXT NOT NULL,
    moderated_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES product_service.categories(category_id) ON DELETE RESTRICT,
    FOREIGN KEY (brand_id) REFERENCES product_service.brands(brand_id) ON DELETE SET NULL
);

-- Таблица для изображений товаров
CREATE TABLE product_service.product_images (
    image_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    url TEXT NOT NULL,
    is_main BOOLEAN DEFAULT FALSE,
    order_index INTEGER DEFAULT 0,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product_service.products(product_id) ON DELETE CASCADE
);

-- Таблица для вариантов товаров
CREATE TABLE product_service.product_variants (
    variant_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    sku_code VARCHAR(100) UNIQUE NOT NULL,
    attributes_json JSONB NOT NULL DEFAULT '{}', -- e.g., {"color": "синий", "size": "L"}
    price_modifier NUMERIC(10, 2) DEFAULT 0, -- Добавка к base_price
    stock_quantity_fbs INTEGER DEFAULT 0 NOT NULL CHECK (stock_quantity_fbs >= 0),
    image_url TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product_service.products(product_id) ON DELETE CASCADE
);
```

-----

#### **4. Order Service (База данных: `order_db`)**

```sql
-- Таблица для заказов
CREATE TABLE order_service.orders (
    order_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
    status order_service.order_status_enum NOT NULL DEFAULT 'PENDING',
    total_amount NUMERIC(10, 2) NOT NULL CHECK (total_amount >= 0),
    discount_amount NUMERIC(10, 2) DEFAULT 0 CHECK (discount_amount >= 0),
    shipping_cost NUMERIC(10, 2) DEFAULT 0 CHECK (shipping_cost >= 0),
    shipping_address_json JSONB NOT NULL, -- Snapshot of address for historical purposes
    payment_method_id BIGINT, -- Logical FK to payment_db.saved_payment_methods(method_id)
    payment_status payment_service.payment_status_enum NOT NULL DEFAULT 'PENDING',
    shipping_method_id BIGINT NOT NULL, -- Logical FK to logistics_db.shipping_methods(method_id)
    tracking_number VARCHAR(255), -- From Logistics Service
    order_type VARCHAR(50) NOT NULL, -- e.g., 'FBO', 'FBS_SELLER_DELIVERY', 'FBS_MARKETPLACE_DELIVERY'
    expected_delivery_date DATE,
    delivered_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для позиций заказа
CREATE TABLE order_service.order_items (
    order_item_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL, -- Logical FK to product_db.products(product_id)
    product_variant_id BIGINT, -- Logical FK to product_db.product_variants(variant_id)
    sku_code VARCHAR(100) NOT NULL, -- Snapshot for historical purposes
    product_name VARCHAR(255) NOT NULL, -- Snapshot for historical purposes
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(10, 2) NOT NULL CHECK (unit_price >= 0),
    total_item_price NUMERIC(10, 2) NOT NULL CHECK (total_item_price >= 0),
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES order_service.orders(order_id) ON DELETE CASCADE
);

-- Таблица для истории статусов заказа
CREATE TABLE order_service.order_status_history (
    history_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    status order_service.order_status_enum NOT NULL,
    changed_by_user_id BIGINT, -- Logical FK to user_db.users(user_id) or admin_db.admin_users(admin_id)
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES order_service.orders(order_id) ON DELETE CASCADE
);
```

-----

#### **5. Review Service (База данных: `review_db`)**

```sql
-- Таблица для отзывов
CREATE TABLE review_service.reviews (
    review_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    product_id BIGINT NOT NULL, -- Logical FK to product_db.products(product_id)
    order_id BIGINT, -- Logical FK to order_db.orders(order_id)
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    is_anonymous BOOLEAN DEFAULT FALSE,
    status review_service.review_status_enum NOT NULL DEFAULT 'PENDING_MODERATION',
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для изображений отзывов
CREATE TABLE review_service.review_images (
    image_id BIGSERIAL PRIMARY KEY,
    review_id BIGINT NOT NULL,
    url TEXT NOT NULL,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (review_id) REFERENCES review_service.reviews(review_id) ON DELETE CASCADE
);

-- Таблица для ответов продавца на отзывы
CREATE TABLE review_service.seller_replies (
    reply_id BIGSERIAL PRIMARY KEY,
    review_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
    reply_text TEXT NOT NULL,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (review_id) REFERENCES review_service.reviews(review_id) ON DELETE CASCADE
);
```

-----

#### **6. Payment Service (База данных: `payment_db`)**

```sql
-- Таблица для платежных транзакций
CREATE TABLE payment_service.payment_transactions (
    transaction_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id BIGINT, -- Logical FK to order_db.orders(order_id)
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    amount NUMERIC(10, 2) NOT NULL CHECK (amount >= 0),
    currency VARCHAR(10) NOT NULL,
    status payment_service.payment_status_enum NOT NULL DEFAULT 'PENDING',
    gateway_transaction_id VARCHAR(255) UNIQUE, -- ID от платежного провайдера
    payment_method_type payment_service.payment_method_type_enum NOT NULL,
    error_message TEXT,
    processed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для сохраненных способов оплаты
CREATE TABLE payment_service.saved_payment_methods (
    method_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    type VARCHAR(50) NOT NULL, -- e.g., 'CARD'
    token TEXT NOT NULL, -- Token from payment provider
    last_four_digits VARCHAR(4) NOT NULL,
    card_brand VARCHAR(50),
    expiry_month INTEGER,
    expiry_year INTEGER,
    is_default BOOLEAN DEFAULT FALSE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, token) -- Пользователь не может добавить один и тот же токен дважды
);

-- Таблица для запросов на выплату продавцу
CREATE TABLE payment_service.payout_requests (
    payout_id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
    amount NUMERIC(10, 2) NOT NULL CHECK (amount > 0),
    status payment_service.payment_status_enum NOT NULL DEFAULT 'PENDING',
    processed_at TIMESTAMP WITH TIME ZONE,
    transaction_id VARCHAR(255), -- ID транзакции выплаты в банке/системе
    notes TEXT,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

-----

#### **7. Notification Service (База данных: `notification_db`)**

```sql
-- Таблица для уведомлений
CREATE TABLE notification_service.notifications (
    notification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    type notification_service.notification_type_enum NOT NULL,
    message_text TEXT NOT NULL,
    link_url TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    sent_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для настроек уведомлений пользователя
CREATE TABLE notification_service.notification_settings (
    user_id BIGINT PRIMARY KEY, -- Logical FK to user_db.users(user_id)
    email_enabled BOOLEAN DEFAULT TRUE,
    sms_enabled BOOLEAN DEFAULT FALSE,
    push_enabled BOOLEAN DEFAULT TRUE,
    order_status_updates BOOLEAN DEFAULT TRUE,
    promotion_updates BOOLEAN DEFAULT TRUE,
    message_notifications BOOLEAN DEFAULT TRUE,
    review_reminders BOOLEAN DEFAULT TRUE,
    wms_alerts BOOLEAN DEFAULT TRUE, -- For sellers/WMS operators
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

-----

#### **8. Logistics Service (База данных: `logistics_db`)**

```sql
-- Таблица для служб доставки
CREATE TABLE logistics_service.shipping_carriers (
    carrier_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    tracking_url_pattern TEXT,
    api_key_encrypted TEXT, -- Encrypted API key for integration
    is_active BOOLEAN DEFAULT TRUE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (carrier_id) REFERENCES logistics_service.shipping_carriers(carrier_id) ON DELETE SET NULL
);

-- Таблица для способов доставки
CREATE TABLE logistics_service.shipping_methods (
    method_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    base_cost NUMERIC(10, 2) NOT NULL CHECK (base_cost >= 0),
    min_delivery_days INTEGER NOT NULL CHECK (min_delivery_days >= 0),
    max_delivery_days INTEGER NOT NULL CHECK (max_delivery_days >= min_delivery_days),
    is_active BOOLEAN DEFAULT TRUE,
    carrier_id BIGINT,
    is_marketplace_delivery BOOLEAN DEFAULT FALSE, -- True if handled by marketplace's WMS/logistics
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (carrier_id) REFERENCES logistics_service.shipping_carriers(carrier_id) ON DELETE SET NULL
);

-- Таблица для пунктов выдачи
CREATE TABLE logistics_service.pickup_points (
    point_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    latitude NUMERIC(10, 7) NOT NULL,
    longitude NUMERIC(10, 7) NOT NULL,
    working_hours_json JSONB DEFAULT '{}', -- e.g., {"Mon": "9:00-18:00", "Tue": "9:00-18:00"}
    is_active BOOLEAN DEFAULT TRUE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

-----

#### **9. WMS Service (База данных: `wms_db`)**

```sql
-- Таблица для складов
CREATE TABLE wms_service.warehouses (
    warehouse_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    address TEXT NOT NULL,
    capacity_sq_m NUMERIC(10, 2),
    is_active BOOLEAN DEFAULT TRUE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для мест хранения (ячеек)
CREATE TABLE wms_service.locations (
    location_id BIGSERIAL PRIMARY KEY,
    warehouse_id BIGINT NOT NULL,
    code VARCHAR(100) NOT NULL, -- e.g., "A-01-01"
    type wms_service.warehouse_location_type_enum NOT NULL,
    max_capacity_units INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (warehouse_id, code), -- Unique location code per warehouse
    FOREIGN KEY (warehouse_id) REFERENCES wms_service.warehouses(warehouse_id) ON DELETE CASCADE
);

-- Таблица для складских позиций (инвентаря)
CREATE TABLE wms_service.inventory_items (
    inventory_item_id BIGSERIAL PRIMARY KEY,
    sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
    warehouse_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    batch_number VARCHAR(255),
    expiration_date DATE,
    seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
    status wms_service.inventory_item_status_enum NOT NULL DEFAULT 'AVAILABLE',
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (sku_code, location_id, batch_number), -- A specific SKU/batch can only be in one location (or multiple if no batch)
    FOREIGN KEY (warehouse_id) REFERENCES wms_service.warehouses(warehouse_id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES wms_service.locations(location_id) ON DELETE RESTRICT
);

-- Таблица для движений запасов
CREATE TABLE wms_service.stock_movements (
    movement_id BIGSERIAL PRIMARY KEY,
    inventory_item_id BIGINT, -- Optional: if specific item is tracked
    sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
    quantity INTEGER NOT NULL,
    from_location_id BIGINT,
    to_location_id BIGINT,
    movement_type wms_service.stock_movement_type_enum NOT NULL,
    reference_document_id BIGINT, -- e.g., order_id, inbound_id, stocktake_id
    reference_document_type VARCHAR(50), -- e.g., 'ORDER', 'INBOUND', 'STOCKTAKE'
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) or admin_db.admin_users(admin_id)
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_location_id) REFERENCES wms_service.locations(location_id) ON DELETE SET NULL,
    FOREIGN KEY (to_location_id) REFERENCES wms_service.locations(location_id) ON DELETE SET NULL
);

-- Таблица для входящих поставок
CREATE TABLE wms_service.inbound_shipments (
    inbound_id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
    warehouse_id BIGINT NOT NULL,
    status wms_service.inbound_status_enum NOT NULL DEFAULT 'PLANNED',
    expected_arrival_date DATE NOT NULL,
    actual_arrival_date TIMESTAMP WITH TIME ZONE,
    notes TEXT,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES wms_service.warehouses(warehouse_id) ON DELETE RESTRICT
);

-- Таблица для позиций входящей поставки
CREATE TABLE wms_service.inbound_items (
    inbound_item_id BIGSERIAL PRIMARY KEY,
    inbound_id BIGINT NOT NULL,
    sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
    expected_quantity INTEGER NOT NULL CHECK (expected_quantity > 0),
    received_quantity INTEGER DEFAULT 0 CHECK (received_quantity >= 0),
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (inbound_id) REFERENCES wms_service.inbound_shipments(inbound_id) ON DELETE CASCADE
);

-- Таблица для заданий на отбор
CREATE TABLE wms_service.picking_tasks (
    task_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL, -- Logical FK to order_db.orders(order_id)
    warehouse_id BIGINT NOT NULL,
    picker_user_id BIGINT, -- Logical FK to user_db.users(user_id) (WMS operator)
    status wms_service.picking_task_status_enum NOT NULL DEFAULT 'NEW',
    assigned_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES wms_service.warehouses(warehouse_id) ON DELETE RESTRICT
);

-- Таблица для позиций задания на отбор
CREATE TABLE wms_service.picking_task_items (
    task_item_id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
    quantity_to_pick INTEGER NOT NULL CHECK (quantity_to_pick > 0),
    picked_quantity INTEGER DEFAULT 0 CHECK (picked_quantity >= 0),
    source_location_id BIGINT, -- Recommended location for picking
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES wms_service.picking_tasks(task_id) ON DELETE CASCADE,
    FOREIGN KEY (source_location_id) REFERENCES wms_service.locations(location_id) ON DELETE SET NULL
);

-- Таблица для исходящих отгрузок
CREATE TABLE wms_service.outbound_shipments (
    outbound_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL, -- Logical FK to order_db.orders(order_id)
    warehouse_id BIGINT NOT NULL,
    status wms_service.outbound_status_enum NOT NULL DEFAULT 'PENDING_PICKING',
    carrier_id BIGINT, -- Logical FK to logistics_db.shipping_carriers(carrier_id)
    tracking_number VARCHAR(255),
    dispatch_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES wms_service.warehouses(warehouse_id) ON DELETE RESTRICT
);

-- Таблица для инвентаризаций
CREATE TABLE wms_service.stocktakes (
    stocktake_id BIGSERIAL PRIMARY KEY,
    warehouse_id BIGINT NOT NULL,
    type wms_service.stocktake_type_enum NOT NULL,
    status wms_service.stocktake_status_enum NOT NULL DEFAULT 'PLANNED',
    start_date DATE NOT NULL,
    end_date DATE,
    initiated_by_user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) (WMS operator)
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES wms_service.warehouses(warehouse_id) ON DELETE RESTRICT
);

-- Таблица для позиций инвентаризации
CREATE TABLE wms_service.stocktake_items (
    stocktake_item_id BIGSERIAL PRIMARY KEY,
    stocktake_id BIGINT NOT NULL,
    sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
    location_id BIGINT,
    system_quantity INTEGER NOT NULL,
    counted_quantity INTEGER NOT NULL,
    discrepancy INTEGER NOT NULL, -- counted_quantity - system_quantity
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (stocktake_id) REFERENCES wms_service.stocktakes(stocktake_id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES wms_service.locations(location_id) ON DELETE SET NULL
);

-- Таблица для внутренних перемещений
CREATE TABLE wms_service.transfers (
    transfer_id BIGSERIAL PRIMARY KEY,
    sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    from_location_id BIGINT NOT NULL,
    to_location_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL, -- e.g., 'PENDING', 'IN_PROGRESS', 'COMPLETED'
    initiated_by_user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) (WMS operator)
    completed_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_location_id) REFERENCES wms_service.locations(location_id) ON DELETE RESTRICT,
    FOREIGN KEY (to_location_id) REFERENCES wms_service.locations(location_id) ON DELETE RESTRICT
);
```

-----

#### **10. Admin Service (База данных: `admin_db`)**

```sql
-- Таблица для администраторов системы
CREATE TABLE admin_service.admin_users (
    admin_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT, -- Logical FK to user_db.users(user_id) (if admin is also a user)
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    roles TEXT[] NOT NULL DEFAULT '{}', -- e.g., ARRAY['SUPER_ADMIN', 'MODERATOR']
    is_active BOOLEAN DEFAULT TRUE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для настроек платформы
CREATE TABLE admin_service.platform_settings (
    setting_id BIGSERIAL PRIMARY KEY,
    setting_key VARCHAR(255) UNIQUE NOT NULL,
    setting_value TEXT NOT NULL,
    value_type admin_service.platform_setting_value_type_enum NOT NULL,
    description TEXT,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для элементов модерации (жалоб, флагов)
CREATE TABLE admin_service.moderation_items (
    moderation_item_id BIGSERIAL PRIMARY KEY,
    item_type admin_service.moderation_item_type_enum NOT NULL,
    item_id BIGINT NOT NULL, -- ID of the item being moderated (product, review, user, etc.)
    reason TEXT NOT NULL,
    status admin_service.moderation_status_enum NOT NULL DEFAULT 'PENDING',
    assigned_to_admin_id BIGINT,
    resolved_at TIMESTAMP WITH TIME ZONE,
    notes TEXT,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (assigned_to_admin_id) REFERENCES admin_service.admin_users(admin_id) ON DELETE SET NULL
);

-- Таблица для журнала активности администраторов
CREATE TABLE admin_service.admin_activity_logs (
    log_id BIGSERIAL PRIMARY KEY,
    admin_user_id BIGINT NOT NULL,
    action TEXT NOT NULL,
    resource_type VARCHAR(100), -- e.g., 'PRODUCT', 'USER', 'ORDER'
    resource_id BIGINT,
    ip_address INET,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_user_id) REFERENCES admin_service.admin_users(admin_id) ON DELETE CASCADE
);
```

-----

#### **11. Seller Service (База данных: `seller_db`)**

```sql
-- Таблица для продавцов
CREATE TABLE seller_service.sellers (
    seller_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL, -- Logical FK to user_db.users(user_id) - owner of the seller account
    shop_name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    logo_url TEXT,
    status seller_service.seller_status_enum NOT NULL DEFAULT 'PENDING_APPROVAL',
    commission_rate NUMERIC(5, 4) NOT NULL CHECK (commission_rate >= 0 AND commission_rate <= 1), -- e.g., 0.15 for 15%
    contact_email VARCHAR(255) NOT NULL,
    contact_phone VARCHAR(50),
    bank_details_json JSONB, -- Encrypted or tokenized bank details
    approval_date TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для сотрудников продавца
CREATE TABLE seller_service.seller_staff (
    staff_id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    user_id BIGINT UNIQUE NOT NULL, -- Logical FK to user_db.users(user_id) - staff member's user account
    role seller_service.seller_staff_role_enum NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    accepted_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES seller_service.sellers(seller_id) ON DELETE CASCADE
);
```

-----

#### **12. Promotion Service (База данных: `promotion_db`)**

```sql
-- Таблица для акций и промокодов
CREATE TABLE promotion_service.promotions (
    promotion_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(100) UNIQUE, -- Promo code, if applicable
    description TEXT,
    discount_type promotion_service.promotion_discount_type_enum NOT NULL,
    discount_value NUMERIC(10, 2) NOT NULL CHECK (discount_value >= 0),
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE NOT NULL,
    usage_limit_total INTEGER, -- NULL means no limit
    usage_limit_per_user INTEGER, -- NULL means no limit per user
    min_order_amount NUMERIC(10, 2) CHECK (min_order_amount >= 0),
    applies_to_category_ids BIGINT[], -- Logical FKs to product_db.categories(category_id)
    applies_to_product_ids BIGINT[], -- Logical FKs to product_db.products(product_id)
    applies_to_seller_ids BIGINT[], -- Logical FKs to seller_db.sellers(seller_id)
    is_active BOOLEAN DEFAULT TRUE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CHECK (end_date >= start_date)
);

-- Таблица для учета использования акций пользователями
CREATE TABLE promotion_service.user_promotion_usage (
    usage_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    promotion_id BIGINT NOT NULL,
    usage_count INTEGER NOT NULL DEFAULT 0 CHECK (usage_count >= 0),
    order_id BIGINT, -- Logical FK to order_db.orders(order_id)
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, promotion_id), -- One entry per user per promotion
    FOREIGN KEY (promotion_id) REFERENCES promotion_service.promotions(promotion_id) ON DELETE CASCADE
);
```

-----

#### **13. QA Service (База данных: `qa_db`)**

```sql
-- Таблица для вопросов к товарам
CREATE TABLE qa_service.questions (
    question_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL, -- Logical FK to product_db.products(product_id)
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) (who asked)
    text TEXT NOT NULL,
    status qa_service.qa_question_status_enum NOT NULL DEFAULT 'PENDING_ANSWER',
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для ответов на вопросы
CREATE TABLE qa_service.answers (
    answer_id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL,
    responder_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) / seller_db.sellers(seller_id) / admin_db.admin_users(admin_id)
    responder_type qa_service.qa_responder_type_enum NOT NULL,
    text TEXT NOT NULL,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES qa_service.questions(question_id) ON DELETE CASCADE
);
```

-----

#### **14. Messaging Service (База данных: `messaging_db`)**

```sql
-- Таблица для чат-бесед (диалогов)
CREATE TABLE messaging_service.chat_conversations (
    conversation_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    participant1_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) or seller_db.sellers(seller_id) or admin_db.admin_users(admin_id)
    participant1_type messaging_service.chat_participant_type_enum NOT NULL,
    participant2_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) or seller_db.sellers(seller_id) or admin_db.admin_users(admin_id)
    participant2_type messaging_service.chat_participant_type_enum NOT NULL,
    subject VARCHAR(255), -- Optional subject for the chat
    is_active BOOLEAN DEFAULT TRUE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (participant1_id, participant1_type, participant2_id, participant2_type) -- Ensures unique conversation between two parties
);

-- Таблица для сообщений в чате
CREATE TABLE messaging_service.chat_messages (
    message_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conversation_id UUID NOT NULL,
    sender_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) or seller_db.sellers(seller_id) or admin_db.admin_users(admin_id)
    sender_type messaging_service.chat_participant_type_enum NOT NULL,
    message_text TEXT NOT NULL,
    is_read_by_recipient BOOLEAN DEFAULT FALSE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES messaging_service.chat_conversations(conversation_id) ON DELETE CASCADE
);
```

-----

#### **15. Search Service (База данных: `search_db`)**

*Этот сервис часто использует специализированные поисковые движки (например, Elasticsearch), а его реляционная база данных может хранить конфигурации или логи, но не индексируемые данные.*

```sql
-- Таблица для конфигурации поисковых индексов
CREATE TABLE search_service.search_index_configs (
    config_id BIGSERIAL PRIMARY KEY,
    index_name VARCHAR(255) UNIQUE NOT NULL,
    source_service VARCHAR(100) NOT NULL, -- Which microservice data is indexed from
    last_indexed_at TIMESTAMP WITH TIME ZONE,
    indexing_schedule VARCHAR(255), -- e.g., cron expression
    is_active BOOLEAN DEFAULT TRUE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для логов поисковых запросов
CREATE TABLE search_service.search_query_logs (
    log_id BIGSERIAL PRIMARY KEY,
    query_text VARCHAR(500) NOT NULL,
    user_id BIGINT, -- Logical FK to user_db.users(user_id)
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    results_count INTEGER,
    ip_address INET
);
```

-----

#### **16. Cart Service (База данных: `cart_db`)**

```sql
-- Таблица для корзин пользователей
CREATE TABLE cart_service.carts (
    cart_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT, -- Logical FK to user_db.users(user_id). Nullable for anonymous carts.
    session_id VARCHAR(255), -- Session ID for anonymous users
    expires_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id), -- A user can only have one active cart
    UNIQUE (session_id) -- A session can only have one active cart
);

-- Таблица для позиций в корзине
CREATE TABLE cart_service.cart_items (
    cart_item_id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL, -- Logical FK to product_db.products(product_id)
    product_variant_id BIGINT, -- Logical FK to product_db.product_variants(variant_id)
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    price_at_add NUMERIC(10, 2) NOT NULL CHECK (price_at_add >= 0),
    added_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES cart_service.carts(cart_id) ON DELETE CASCADE,
    UNIQUE (cart_id, product_id, product_variant_id) -- A specific product/variant can only be once in a cart
);
```