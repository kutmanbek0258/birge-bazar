Для преобразования сущностей базы данных в формат PostgreSQL, я предоставлю SQL-скрипты `CREATE TABLE` для каждого микросервиса. Это включает определение типов данных, первичных и уникальных ключей, а также ограничений `NOT NULL`.

Важно помнить, что в микросервисной архитектуре, хотя я и укажу "логические" внешние ключи между сервисами для ясности, физические `FOREIGN KEY` ограничения в базе данных обычно не создаются между таблицами, принадлежащими разным микросервисам. Целостность данных в таких случаях поддерживается на уровне бизнес-логики и межсервисного взаимодействия (например, с использованием паттерна "Event Sourcing" или "Saga").

Я буду использовать `BIGINT` для большинства ID (для обеспечения высокой масштабируемости, хотя `UUID` также является хорошим выбором для глобальной уникальности), `TIMESTAMP WITH TIME ZONE` для временных меток и `NUMERIC` для денежных значений.

-----

### **Сущности Баз Данных в Формате PostgreSQL**

-----

#### **Общие ENUM типы (если используются несколькими сервисами)**

В реальной системе ENUM'ы часто определяются в конкретных сервисах, но для демонстрации можно представить общие.

```sql
-- Общие ENUM типы (могут быть определены в отдельных сервисах)
CREATE TYPE user_status_enum AS ENUM ('ACTIVE', 'BLOCKED', 'PENDING_VERIFICATION');
CREATE TYPE gender_enum AS ENUM ('MALE', 'FEMALE', 'OTHER');
CREATE TYPE order_status_enum AS ENUM ('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'REFUNDED');
CREATE TYPE payment_status_enum AS ENUM ('PENDING', 'PAID', 'FAILED', 'REFUNDED', 'AUTHORIZED');
CREATE TYPE payment_method_type_enum AS ENUM ('CARD', 'CASH_ON_DELIVERY', 'E_WALLET');
CREATE TYPE review_status_enum AS ENUM ('PENDING_MODERATION', 'APPROVED', 'REJECTED', 'HIDDEN');
CREATE TYPE notification_type_enum AS ENUM ('ORDER_STATUS', 'PROMOTION', 'MESSAGE', 'REVIEW_REMINDER', 'WMS_ALERT');
CREATE TYPE shipping_method_type_enum AS ENUM ('COURIER', 'PICKUP', 'POSTAMAT');

CREATE TYPE warehouse_location_type_enum AS ENUM ('SHELF', 'PALLET', 'FLOOR', 'BIN');
CREATE TYPE inventory_item_status_enum AS ENUM ('AVAILABLE', 'RESERVED', 'QUARANTINE', 'DAMAGED');
CREATE TYPE stock_movement_type_enum AS ENUM ('RECEIVE', 'PICK', 'SHIP', 'TRANSFER', 'ADJUSTMENT_IN', 'ADJUSTMENT_OUT');
CREATE TYPE inbound_status_enum AS ENUM ('PLANNED', 'IN_TRANSIT', 'RECEIVED_PARTIAL', 'RECEIVED_COMPLETED', 'CANCELLED');
CREATE TYPE picking_task_status_enum AS ENUM ('NEW', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');
CREATE TYPE outbound_status_enum AS ENUM ('PENDING_PICKING', 'READY_FOR_DISPATCH', 'DISPATCHED', 'DELIVERED');
CREATE TYPE stocktake_status_enum AS ENUM ('PLANNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');
CREATE TYPE stocktake_type_enum AS ENUM ('FULL', 'CYCLE_COUNT');

CREATE TYPE moderation_item_type_enum AS ENUM ('PRODUCT', 'REVIEW', 'SELLER', 'USER', 'COMPLAINT');
CREATE TYPE moderation_status_enum AS ENUM ('PENDING', 'IN_REVIEW', 'APPROVED', 'REJECTED', 'ACTION_TAKEN');
CREATE TYPE platform_setting_value_type_enum AS ENUM ('STRING', 'NUMBER', 'BOOLEAN', 'JSON');
CREATE TYPE seller_status_enum AS ENUM ('PENDING_APPROVAL', 'ACTIVE', 'BLOCKED', 'SUSPENDED');
CREATE TYPE seller_staff_role_enum AS ENUM ('MANAGER', 'PRODUCT_EDITOR', 'ORDER_PROCESSOR', 'FINANCIER', 'MESSENGER');
CREATE TYPE promotion_discount_type_enum AS ENUM ('PERCENTAGE', 'FIXED_AMOUNT', 'FREE_SHIPPING');
CREATE TYPE qa_question_status_enum AS ENUM ('PENDING_ANSWER', 'ANSWERED', 'HIDDEN');
CREATE TYPE qa_responder_type_enum AS ENUM ('USER', 'SELLER', 'ADMIN');
CREATE TYPE chat_participant_type_enum AS ENUM ('USER', 'SELLER', 'ADMIN');
CREATE TYPE product_status_enum AS ENUM ('ACTIVE', 'DRAFT', 'PENDING_MODERATION', 'REJECTED', 'ARCHIVED');
CREATE TYPE auth_two_factor_method_enum AS ENUM ('TOTP', 'SMS', 'EMAIL');
CREATE TYPE auth_login_status_enum AS ENUM ('SUCCESS', 'FAILED');
```

-----

#### **1. User Service (База данных: `user_db`)**

```sql
-- Таблица для пользователей
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(50) UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    gender gender_enum,
    avatar_url TEXT,
    status user_status_enum NOT NULL DEFAULT 'PENDING_VERIFICATION',
    registration_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_activity_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для адресов пользователей
CREATE TABLE user_addresses (
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
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Таблица для избранных товаров
CREATE TABLE favorite_products (
    favorite_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL, -- Logical FK to product_db.products(product_id)
    added_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, product_id), -- Пользователь может добавить один товар в избранное только один раз
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
```

-----

#### **2. Auth Service (База данных: `auth_db`)**

```sql
-- Таблица для учетных данных пользователя
CREATE TABLE user_credentials (
    user_id BIGINT PRIMARY KEY, -- Logical FK to user_db.users(user_id)
    password_hash TEXT NOT NULL,
    last_password_change_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    email_verified BOOLEAN DEFAULT FALSE,
    phone_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для сессий пользователя
CREATE TABLE user_sessions (
    session_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    jwt_token TEXT, -- Optional: if session tokens are managed server-side
    refresh_token UUID UNIQUE NOT NULL DEFAULT gen_random_uuid(),
    device_info TEXT,
    ip_address INET NOT NULL, -- Or VARCHAR(45) for IPv6
    login_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для настроек двухфакторной аутентификации
CREATE TABLE two_factor_auth_settings (
    user_id BIGINT PRIMARY KEY, -- Logical FK to user_db.users(user_id)
    is_enabled BOOLEAN DEFAULT FALSE NOT NULL,
    method auth_two_factor_method_enum,
    totp_secret VARCHAR(255),
    sms_code_last_sent_at TIMESTAMP WITH TIME ZONE,
    email_code_last_sent_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для истории входов
CREATE TABLE login_history (
    entry_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    login_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    ip_address INET NOT NULL,
    device_info TEXT,
    status auth_login_status_enum NOT NULL
);
```

-----

#### **3. Product Service (База данных: `product_db`)**

```sql
-- Таблица для категорий товаров
CREATE TABLE categories (
    category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT,
    slug VARCHAR(255) UNIQUE NOT NULL,
    icon_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES categories(category_id) ON DELETE SET NULL
);

-- Таблица для брендов
CREATE TABLE brands (
    brand_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    logo_url TEXT,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для товаров
CREATE TABLE products (
    product_id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    short_description VARCHAR(500),
    base_price NUMERIC(10, 2) NOT NULL CHECK (base_price >= 0),
    category_id BIGINT NOT NULL,
    brand_id BIGINT,
    status product_status_enum NOT NULL DEFAULT 'DRAFT',
    main_image_url TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    moderated_at TIMESTAMP WITH TIME ZONE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT,
    FOREIGN KEY (brand_id) REFERENCES brands(brand_id) ON DELETE SET NULL
);

-- Таблица для изображений товаров
CREATE TABLE product_images (
    image_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    url TEXT NOT NULL,
    is_main BOOLEAN DEFAULT FALSE,
    order_index INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- Таблица для вариантов товаров
CREATE TABLE product_variants (
    variant_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    sku_code VARCHAR(100) UNIQUE NOT NULL,
    attributes_json JSONB NOT NULL DEFAULT '{}', -- e.g., {"color": "синий", "size": "L"}
    price_modifier NUMERIC(10, 2) DEFAULT 0, -- Добавка к base_price
    stock_quantity_fbs INTEGER DEFAULT 0 NOT NULL CHECK (stock_quantity_fbs >= 0),
    image_url TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);
```

-----

#### **4. Order Service (База данных: `order_db`)**

```sql
-- Таблица для заказов
CREATE TABLE orders (
    order_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
    status order_status_enum NOT NULL DEFAULT 'PENDING',
    total_amount NUMERIC(10, 2) NOT NULL CHECK (total_amount >= 0),
    discount_amount NUMERIC(10, 2) DEFAULT 0 CHECK (discount_amount >= 0),
    shipping_cost NUMERIC(10, 2) DEFAULT 0 CHECK (shipping_cost >= 0),
    shipping_address_json JSONB NOT NULL, -- Snapshot of address for historical purposes
    payment_method_id BIGINT, -- Logical FK to payment_db.saved_payment_methods(method_id)
    payment_status payment_status_enum NOT NULL DEFAULT 'PENDING',
    shipping_method_id BIGINT NOT NULL, -- Logical FK to logistics_db.shipping_methods(method_id)
    tracking_number VARCHAR(255), -- From Logistics Service
    order_type VARCHAR(50) NOT NULL, -- e.g., 'FBO', 'FBS_SELLER_DELIVERY', 'FBS_MARKETPLACE_DELIVERY'
    expected_delivery_date DATE,
    ordered_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    delivered_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для позиций заказа
CREATE TABLE order_items (
    order_item_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL, -- Logical FK to product_db.products(product_id)
    product_variant_id BIGINT, -- Logical FK to product_db.product_variants(variant_id)
    sku_code VARCHAR(100) NOT NULL, -- Snapshot for historical purposes
    product_name VARCHAR(255) NOT NULL, -- Snapshot for historical purposes
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(10, 2) NOT NULL CHECK (unit_price >= 0),
    total_item_price NUMERIC(10, 2) NOT NULL CHECK (total_item_price >= 0),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- Таблица для истории статусов заказа
CREATE TABLE order_status_history (
    history_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    status order_status_enum NOT NULL,
    changed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    changed_by_user_id BIGINT, -- Logical FK to user_db.users(user_id) or admin_db.admin_users(admin_id)
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);
```

-----

#### **5. Review Service (База данных: `review_db`)**

```sql
-- Таблица для отзывов
CREATE TABLE reviews (
    review_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    product_id BIGINT NOT NULL, -- Logical FK to product_db.products(product_id)
    order_id BIGINT, -- Logical FK to order_db.orders(order_id)
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    is_anonymous BOOLEAN DEFAULT FALSE,
    status review_status_enum NOT NULL DEFAULT 'PENDING_MODERATION',
    reviewed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для изображений отзывов
CREATE TABLE review_images (
    image_id BIGSERIAL PRIMARY KEY,
    review_id BIGINT NOT NULL,
    url TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (review_id) REFERENCES reviews(review_id) ON DELETE CASCADE
);

-- Таблица для ответов продавца на отзывы
CREATE TABLE seller_replies (
    reply_id BIGSERIAL PRIMARY KEY,
    review_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
    reply_text TEXT NOT NULL,
    replied_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (review_id) REFERENCES reviews(review_id) ON DELETE CASCADE
);
```

-----

#### **6. Payment Service (База данных: `payment_db`)**

```sql
-- Таблица для платежных транзакций
CREATE TABLE payment_transactions (
    transaction_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id BIGINT, -- Logical FK to order_db.orders(order_id)
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    amount NUMERIC(10, 2) NOT NULL CHECK (amount >= 0),
    currency VARCHAR(10) NOT NULL,
    status payment_status_enum NOT NULL DEFAULT 'PENDING',
    gateway_transaction_id VARCHAR(255) UNIQUE, -- ID от платежного провайдера
    payment_method_type payment_method_type_enum NOT NULL,
    error_message TEXT,
    processed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для сохраненных способов оплаты
CREATE TABLE saved_payment_methods (
    method_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    type VARCHAR(50) NOT NULL, -- e.g., 'CARD'
    token TEXT NOT NULL, -- Token from payment provider
    last_four_digits VARCHAR(4) NOT NULL,
    card_brand VARCHAR(50),
    expiry_month INTEGER,
    expiry_year INTEGER,
    is_default BOOLEAN DEFAULT FALSE,
    added_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, token) -- Пользователь не может добавить один и тот же токен дважды
);

-- Таблица для запросов на выплату продавцу
CREATE TABLE payout_requests (
    payout_id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
    amount NUMERIC(10, 2) NOT NULL CHECK (amount > 0),
    status payment_status_enum NOT NULL DEFAULT 'PENDING',
    requested_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP WITH TIME ZONE,
    transaction_id VARCHAR(255), -- ID транзакции выплаты в банке/системе
    notes TEXT
);
```

-----

#### **7. Notification Service (База данных: `notification_db`)**

```sql
-- Таблица для уведомлений
CREATE TABLE notifications (
    notification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    type notification_type_enum NOT NULL,
    message_text TEXT NOT NULL,
    link_url TEXT,
    is_read BOOLEAN DEFAULT FALSE,
    sent_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для настроек уведомлений пользователя
CREATE TABLE notification_settings (
    user_id BIGINT PRIMARY KEY, -- Logical FK to user_db.users(user_id)
    email_enabled BOOLEAN DEFAULT TRUE,
    sms_enabled BOOLEAN DEFAULT FALSE,
    push_enabled BOOLEAN DEFAULT TRUE,
    order_status_updates BOOLEAN DEFAULT TRUE,
    promotion_updates BOOLEAN DEFAULT TRUE,
    message_notifications BOOLEAN DEFAULT TRUE,
    review_reminders BOOLEAN DEFAULT TRUE,
    wms_alerts BOOLEAN DEFAULT TRUE, -- For sellers/WMS operators
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

-----

#### **8. Logistics Service (База данных: `logistics_db`)**

```sql
-- Таблица для служб доставки
CREATE TABLE shipping_carriers (
    carrier_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    tracking_url_pattern TEXT,
    api_key_encrypted TEXT, -- Encrypted API key for integration
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для способов доставки
CREATE TABLE shipping_methods (
    method_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    base_cost NUMERIC(10, 2) NOT NULL CHECK (base_cost >= 0),
    min_delivery_days INTEGER NOT NULL CHECK (min_delivery_days >= 0),
    max_delivery_days INTEGER NOT NULL CHECK (max_delivery_days >= min_delivery_days),
    is_active BOOLEAN DEFAULT TRUE,
    carrier_id BIGINT,
    is_marketplace_delivery BOOLEAN DEFAULT FALSE, -- True if handled by marketplace's WMS/logistics
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (carrier_id) REFERENCES shipping_carriers(carrier_id) ON DELETE SET NULL
);

-- Таблица для пунктов выдачи
CREATE TABLE pickup_points (
    point_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    latitude NUMERIC(10, 7) NOT NULL,
    longitude NUMERIC(10, 7) NOT NULL,
    working_hours_json JSONB DEFAULT '{}', -- e.g., {"Mon": "9:00-18:00", "Tue": "9:00-18:00"}
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

-----

#### **9. WMS Service (База данных: `wms_db`)**

```sql
-- Таблица для складов
CREATE TABLE warehouses (
    warehouse_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    address TEXT NOT NULL,
    capacity_sq_m NUMERIC(10, 2),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для мест хранения (ячеек)
CREATE TABLE locations (
    location_id BIGSERIAL PRIMARY KEY,
    warehouse_id BIGINT NOT NULL,
    code VARCHAR(100) NOT NULL, -- e.g., "A-01-01"
    type warehouse_location_type_enum NOT NULL,
    max_capacity_units INTEGER,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (warehouse_id, code), -- Unique location code per warehouse
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id) ON DELETE CASCADE
);

-- Таблица для складских позиций (инвентаря)
CREATE TABLE inventory_items (
    inventory_item_id BIGSERIAL PRIMARY KEY,
    sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
    warehouse_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    batch_number VARCHAR(255),
    expiration_date DATE,
    seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
    status inventory_item_status_enum NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (sku_code, location_id, batch_number), -- A specific SKU/batch can only be in one location (or multiple if no batch)
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES locations(location_id) ON DELETE RESTRICT
);

-- Таблица для движений запасов
CREATE TABLE stock_movements (
    movement_id BIGSERIAL PRIMARY KEY,
    inventory_item_id BIGINT, -- Optional: if specific item is tracked
    sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
    quantity INTEGER NOT NULL,
    from_location_id BIGINT,
    to_location_id BIGINT,
    movement_type stock_movement_type_enum NOT NULL,
    reference_document_id BIGINT, -- e.g., order_id, inbound_id, stocktake_id
    reference_document_type VARCHAR(50), -- e.g., 'ORDER', 'INBOUND', 'STOCKTAKE'
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) or admin_db.admin_users(admin_id)
    moved_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_location_id) REFERENCES locations(location_id) ON DELETE SET NULL,
    FOREIGN KEY (to_location_id) REFERENCES locations(location_id) ON DELETE SET NULL
);

-- Таблица для входящих поставок
CREATE TABLE inbound_shipments (
    inbound_id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
    warehouse_id BIGINT NOT NULL,
    status inbound_status_enum NOT NULL DEFAULT 'PLANNED',
    expected_arrival_date DATE NOT NULL,
    actual_arrival_date TIMESTAMP WITH TIME ZONE,
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id) ON DELETE RESTRICT
);

-- Таблица для позиций входящей поставки
CREATE TABLE inbound_items (
    inbound_item_id BIGSERIAL PRIMARY KEY,
    inbound_id BIGINT NOT NULL,
    sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
    expected_quantity INTEGER NOT NULL CHECK (expected_quantity > 0),
    received_quantity INTEGER DEFAULT 0 CHECK (received_quantity >= 0),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (inbound_id) REFERENCES inbound_shipments(inbound_id) ON DELETE CASCADE
);

-- Таблица для заданий на отбор
CREATE TABLE picking_tasks (
    task_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL, -- Logical FK to order_db.orders(order_id)
    warehouse_id BIGINT NOT NULL,
    picker_user_id BIGINT, -- Logical FK to user_db.users(user_id) (WMS operator)
    status picking_task_status_enum NOT NULL DEFAULT 'NEW',
    assigned_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id) ON DELETE RESTRICT
);

-- Таблица для позиций задания на отбор
CREATE TABLE picking_task_items (
    task_item_id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
    quantity_to_pick INTEGER NOT NULL CHECK (quantity_to_pick > 0),
    picked_quantity INTEGER DEFAULT 0 CHECK (picked_quantity >= 0),
    source_location_id BIGINT, -- Recommended location for picking
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES picking_tasks(task_id) ON DELETE CASCADE,
    FOREIGN KEY (source_location_id) REFERENCES locations(location_id) ON DELETE SET NULL
);

-- Таблица для исходящих отгрузок
CREATE TABLE outbound_shipments (
    outbound_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL, -- Logical FK to order_db.orders(order_id)
    warehouse_id BIGINT NOT NULL,
    status outbound_status_enum NOT NULL DEFAULT 'PENDING_PICKING',
    carrier_id BIGINT, -- Logical FK to logistics_db.shipping_carriers(carrier_id)
    tracking_number VARCHAR(255),
    dispatch_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id) ON DELETE RESTRICT
);

-- Таблица для инвентаризаций
CREATE TABLE stocktakes (
    stocktake_id BIGSERIAL PRIMARY KEY,
    warehouse_id BIGINT NOT NULL,
    type stocktake_type_enum NOT NULL,
    status stocktake_status_enum NOT NULL DEFAULT 'PLANNED',
    start_date DATE NOT NULL,
    end_date DATE,
    initiated_by_user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) (WMS operator)
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id) ON DELETE RESTRICT
);

-- Таблица для позиций инвентаризации
CREATE TABLE stocktake_items (
    stocktake_item_id BIGSERIAL PRIMARY KEY,
    stocktake_id BIGINT NOT NULL,
    sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
    location_id BIGINT,
    system_quantity INTEGER NOT NULL,
    counted_quantity INTEGER NOT NULL,
    discrepancy INTEGER NOT NULL, -- counted_quantity - system_quantity
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (stocktake_id) REFERENCES stocktakes(stocktake_id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES locations(location_id) ON DELETE SET NULL
);

-- Таблица для внутренних перемещений
CREATE TABLE transfers (
    transfer_id BIGSERIAL PRIMARY KEY,
    sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    from_location_id BIGINT NOT NULL,
    to_location_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL, -- e.g., 'PENDING', 'IN_PROGRESS', 'COMPLETED'
    initiated_by_user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) (WMS operator)
    completed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_location_id) REFERENCES locations(location_id) ON DELETE RESTRICT,
    FOREIGN KEY (to_location_id) REFERENCES locations(location_id) ON DELETE RESTRICT
);
```

-----

#### **10. Admin Service (База данных: `admin_db`)**

```sql
-- Таблица для администраторов системы
CREATE TABLE admin_users (
    admin_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT, -- Logical FK to user_db.users(user_id) (if admin is also a user)
    username VARCHAR(100) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    roles TEXT[] NOT NULL DEFAULT '{}', -- e.g., ARRAY['SUPER_ADMIN', 'MODERATOR']
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для настроек платформы
CREATE TABLE platform_settings (
    setting_id BIGSERIAL PRIMARY KEY,
    setting_key VARCHAR(255) UNIQUE NOT NULL,
    setting_value TEXT NOT NULL,
    value_type platform_setting_value_type_enum NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для элементов модерации (жалоб, флагов)
CREATE TABLE moderation_items (
    moderation_item_id BIGSERIAL PRIMARY KEY,
    item_type moderation_item_type_enum NOT NULL,
    item_id BIGINT NOT NULL, -- ID of the item being moderated (product, review, user, etc.)
    reason TEXT NOT NULL,
    status moderation_status_enum NOT NULL DEFAULT 'PENDING',
    assigned_to_admin_id BIGINT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP WITH TIME ZONE,
    notes TEXT,
    FOREIGN KEY (assigned_to_admin_id) REFERENCES admin_users(admin_id) ON DELETE SET NULL
);

-- Таблица для журнала активности администраторов
CREATE TABLE admin_activity_logs (
    log_id BIGSERIAL PRIMARY KEY,
    admin_user_id BIGINT NOT NULL,
    action TEXT NOT NULL,
    resource_type VARCHAR(100), -- e.g., 'PRODUCT', 'USER', 'ORDER'
    resource_id BIGINT,
    ip_address INET,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_user_id) REFERENCES admin_users(admin_id) ON DELETE CASCADE
);
```

-----

#### **11. Seller Service (База данных: `seller_db`)**

```sql
-- Таблица для продавцов
CREATE TABLE sellers (
    seller_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL, -- Logical FK to user_db.users(user_id) - owner of the seller account
    shop_name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    logo_url TEXT,
    status seller_status_enum NOT NULL DEFAULT 'PENDING_APPROVAL',
    commission_rate NUMERIC(5, 4) NOT NULL CHECK (commission_rate >= 0 AND commission_rate <= 1), -- e.g., 0.15 for 15%
    contact_email VARCHAR(255) NOT NULL,
    contact_phone VARCHAR(50),
    bank_details_json JSONB, -- Encrypted or tokenized bank details
    registration_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    approval_date TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для сотрудников продавца
CREATE TABLE seller_staff (
    staff_id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    user_id BIGINT UNIQUE NOT NULL, -- Logical FK to user_db.users(user_id) - staff member's user account
    role seller_staff_role_enum NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    invited_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    accepted_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES sellers(seller_id) ON DELETE CASCADE
);
```

-----

#### **12. Promotion Service (База данных: `promotion_db`)**

```sql
-- Таблица для акций и промокодов
CREATE TABLE promotions (
    promotion_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(100) UNIQUE, -- Promo code, if applicable
    description TEXT,
    discount_type promotion_discount_type_enum NOT NULL,
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
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CHECK (end_date >= start_date)
);

-- Таблица для учета использования акций пользователями
CREATE TABLE user_promotion_usage (
    usage_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    promotion_id BIGINT NOT NULL,
    usage_count INTEGER NOT NULL DEFAULT 0 CHECK (usage_count >= 0),
    last_used_at TIMESTAMP WITH TIME ZONE,
    order_id BIGINT, -- Logical FK to order_db.orders(order_id)
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, promotion_id), -- One entry per user per promotion
    FOREIGN KEY (promotion_id) REFERENCES promotions(promotion_id) ON DELETE CASCADE
);
```

-----

#### **13. QA Service (База данных: `qa_db`)**

```sql
-- Таблица для вопросов к товарам
CREATE TABLE questions (
    question_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL, -- Logical FK to product_db.products(product_id)
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) (who asked)
    text TEXT NOT NULL,
    status qa_question_status_enum NOT NULL DEFAULT 'PENDING_ANSWER',
    asked_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для ответов на вопросы
CREATE TABLE answers (
    answer_id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL,
    responder_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) / seller_db.sellers(seller_id) / admin_db.admin_users(admin_id)
    responder_type qa_responder_type_enum NOT NULL,
    text TEXT NOT NULL,
    answered_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE
);
```

-----

#### **14. Messaging Service (База данных: `messaging_db`)**

```sql
-- Таблица для чат-бесед (диалогов)
CREATE TABLE chat_conversations (
    conversation_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    participant1_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) or seller_db.sellers(seller_id) or admin_db.admin_users(admin_id)
    participant1_type chat_participant_type_enum NOT NULL,
    participant2_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) or seller_db.sellers(seller_id) or admin_db.admin_users(admin_id)
    participant2_type chat_participant_type_enum NOT NULL,
    subject VARCHAR(255), -- Optional subject for the chat
    last_message_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (participant1_id, participant1_type, participant2_id, participant2_type) -- Ensures unique conversation between two parties
);

-- Таблица для сообщений в чате
CREATE TABLE chat_messages (
    message_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    conversation_id UUID NOT NULL,
    sender_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) or seller_db.sellers(seller_id) or admin_db.admin_users(admin_id)
    sender_type chat_participant_type_enum NOT NULL,
    message_text TEXT NOT NULL,
    sent_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_read_by_recipient BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (conversation_id) REFERENCES chat_conversations(conversation_id) ON DELETE CASCADE
);
```

-----

#### **15. Search Service (База данных: `search_db`)**

*Этот сервис часто использует специализированные поисковые движки (например, Elasticsearch), а его реляционная база данных может хранить конфигурации или логи, но не индексируемые данные.*

```sql
-- Таблица для конфигурации поисковых индексов
CREATE TABLE search_index_configs (
    config_id BIGSERIAL PRIMARY KEY,
    index_name VARCHAR(255) UNIQUE NOT NULL,
    source_service VARCHAR(100) NOT NULL, -- Which microservice data is indexed from
    last_indexed_at TIMESTAMP WITH TIME ZONE,
    indexing_schedule VARCHAR(255), -- e.g., cron expression
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для логов поисковых запросов
CREATE TABLE search_query_logs (
    log_id BIGSERIAL PRIMARY KEY,
    query_text VARCHAR(500) NOT NULL,
    user_id BIGINT, -- Logical FK to user_db.users(user_id)
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    results_count INTEGER,
    ip_address INET
);
```