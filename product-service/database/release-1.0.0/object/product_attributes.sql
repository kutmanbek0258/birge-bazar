CREATE TABLE products.product_attributes (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    attribute_name VARCHAR(128),
    attribute_value VARCHAR(255)
);

