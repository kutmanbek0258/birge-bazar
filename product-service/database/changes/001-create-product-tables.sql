--liquibase formatted sql

--changeset your_name:1
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

--changeset your_name:2
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

--changeset your_name:3
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

--changeset your_name:4
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

--changeset your_name:5
CREATE TABLE product_service.product_variants (
    variant_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    sku_code VARCHAR(100) UNIQUE NOT NULL,
    attributes_json JSONB NOT NULL DEFAULT '{}', -- e.g., {"color": "синий", "Размер": "L"}
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
