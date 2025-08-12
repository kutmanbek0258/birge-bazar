--liquibase formatted sql

--changeset your_name:1
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

--changeset your_name:2
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
