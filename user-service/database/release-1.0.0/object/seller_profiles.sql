CREATE TABLE users.seller_profiles (
     id BIGSERIAL PRIMARY KEY,
     user_id BIGINT UNIQUE NOT NULL,
     shop_name VARCHAR(255),
     description TEXT,
     legal_info TEXT,
     delivery_terms TEXT,
     fulfillment_schema VARCHAR(8) DEFAULT 'FBS' -- FBO, FBS, гибрид
);