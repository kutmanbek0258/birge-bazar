CREATE TABLE products.products (
    id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL, -- user_id продавца
    category_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(12,2),
    discount_price NUMERIC(12,2),
    status VARCHAR(32),
    fbs_stock INT,       -- доступно у продавца (FBS)
    fbo_stock INT,       -- доступно на складе маркетплейса (FBO)
    fulfillment_schema VARCHAR(8) DEFAULT 'FBS', -- FBO, FBS, гибрид
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

