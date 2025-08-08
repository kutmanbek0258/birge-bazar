--liquibase formatted sql

--changeset carts:tables-1

CREATE SCHEMA carts;

-- Таблица для корзин
CREATE TABLE carts.carts (
    cart_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT, -- Logical FK to user_db.users(user_id)
    session_id VARCHAR(255), -- For anonymous users
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для элементов корзины
CREATE TABLE carts.cart_items (
    cart_item_id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL, -- Logical FK to product_db.products(product_id)
    product_variant_id BIGINT, -- Logical FK to product_db.product_variants(variant_id)
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES carts.carts(cart_id) ON DELETE CASCADE
);
