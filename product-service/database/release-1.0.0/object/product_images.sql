CREATE TABLE products.product_images (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    image_url VARCHAR(512) NOT NULL,
    is_main BOOLEAN DEFAULT FALSE
);

