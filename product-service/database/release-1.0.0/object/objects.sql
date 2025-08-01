CREATE TYPE products.product_status_enum AS ENUM ('ACTIVE', 'DRAFT', 'PENDING_MODERATION', 'REJECTED', 'ARCHIVED');

-- Таблица для категорий товаров
CREATE TABLE products.categories (
                            category_id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            parent_id BIGINT,
                            slug VARCHAR(255) UNIQUE NOT NULL,
                            icon_url TEXT,
                            created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (parent_id) REFERENCES products.categories(category_id) ON DELETE SET NULL
);

-- Таблица для брендов
CREATE TABLE products.brands (
                        brand_id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) UNIQUE NOT NULL,
                        logo_url TEXT,
                        description TEXT,
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для товаров
CREATE TABLE products.products (
                          product_id BIGSERIAL PRIMARY KEY,
                          seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
                          name VARCHAR(255) NOT NULL,
                          description TEXT NOT NULL,
                          short_description VARCHAR(500),
                          base_price NUMERIC(10, 2) NOT NULL CHECK (base_price >= 0),
                          category_id BIGINT NOT NULL,
                          brand_id BIGINT,
                          status products.product_status_enum NOT NULL DEFAULT 'DRAFT',
                          main_image_url TEXT NOT NULL,
                          created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                          moderated_at TIMESTAMP WITH TIME ZONE,
                          FOREIGN KEY (category_id) REFERENCES products.categories(category_id) ON DELETE RESTRICT,
                          FOREIGN KEY (brand_id) REFERENCES products.brands(brand_id) ON DELETE SET NULL
);

-- Таблица для изображений товаров
CREATE TABLE products.product_images (
                                image_id BIGSERIAL PRIMARY KEY,
                                product_id BIGINT NOT NULL,
                                url TEXT NOT NULL,
                                is_main BOOLEAN DEFAULT FALSE,
                                order_index INTEGER DEFAULT 0,
                                created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (product_id) REFERENCES products.products(product_id) ON DELETE CASCADE
);

-- Таблица для вариантов товаров
CREATE TABLE products.product_variants (
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
                                  FOREIGN KEY (product_id) REFERENCES products.products(product_id) ON DELETE CASCADE
);
