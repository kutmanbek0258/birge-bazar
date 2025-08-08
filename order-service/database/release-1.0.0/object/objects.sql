--liquibase formatted sql

--changeset orders:tables-1

-- Таблица для заказов
CREATE TABLE orders.orders (
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
    delivered_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для позиций заказа
CREATE TABLE orders.order_items (
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
    FOREIGN KEY (order_id) REFERENCES orders.orders(order_id) ON DELETE CASCADE
);

-- Таблица для истории статусов заказа
CREATE TABLE orders.order_status_history (
    history_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    status order_status_enum NOT NULL,
    changed_by_user_id BIGINT, -- Logical FK to user_db.users(user_id) or admin_db.admin_users(admin_id)
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders.orders(order_id) ON DELETE CASCADE
);
