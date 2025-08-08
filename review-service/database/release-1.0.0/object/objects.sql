--liquibase formatted sql

--changeset reviews:tables-1

-- Enum for review status
CREATE TYPE reviews.review_status_enum AS ENUM (
    'PENDING_MODERATION',
    'APPROVED',
    'REJECTED'
);

-- Таблица для отзывов
CREATE TABLE reviews.reviews (
    review_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    product_id BIGINT NOT NULL, -- Logical FK to product_db.products(product_id)
    order_id BIGINT, -- Logical FK to order_db.orders(order_id)
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    is_anonymous BOOLEAN DEFAULT FALSE,
    status reviews.review_status_enum NOT NULL DEFAULT 'PENDING_MODERATION',
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для изображений отзывов
CREATE TABLE reviews.review_images (
    image_id BIGSERIAL PRIMARY KEY,
    review_id BIGINT NOT NULL,
    url TEXT NOT NULL,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (review_id) REFERENCES reviews.reviews(review_id) ON DELETE CASCADE
);

-- Таблица для ответов продавца на отзывы
CREATE TABLE reviews.seller_replies (
    reply_id BIGSERIAL PRIMARY KEY,
    review_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
    reply_text TEXT NOT NULL,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (review_id) REFERENCES reviews.reviews(review_id) ON DELETE CASCADE
);
