-- ENUM types for user-service
CREATE TYPE user_service.user_status_enum AS ENUM ('ACTIVE', 'BLOCKED', 'PENDING_VERIFICATION');
CREATE TYPE user_service.gender_enum AS ENUM ('MALE', 'FEMALE', 'OTHER');

-- Table for users
CREATE TABLE user_service.users (
    user_id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(50) UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    gender user_service.gender_enum,
    avatar_url TEXT,
    status user_service.user_status_enum NOT NULL DEFAULT 'PENDING_VERIFICATION',
    last_activity_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Table for user addresses
CREATE TABLE user_service.user_addresses (
    address_id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    label VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    street VARCHAR(255) NOT NULL,
    house_number VARCHAR(50) NOT NULL,
    apartment_number VARCHAR(50),
    zip_code VARCHAR(20),
    is_default BOOLEAN DEFAULT FALSE,
    latitude NUMERIC(10, 7),
    longitude NUMERIC(10, 7),
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_service.users(user_id) ON DELETE CASCADE
);

-- Table for favorite products
CREATE TABLE user_service.favorite_products (
    favorite_id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    product_id BIGINT NOT NULL, -- Logical FK to product_db.products(product_id)
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES user_service.users(user_id) ON DELETE CASCADE
);