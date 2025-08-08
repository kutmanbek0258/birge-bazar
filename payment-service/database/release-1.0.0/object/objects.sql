--liquibase formatted sql

--changeset payments:tables-1

-- Таблица для платежных транзакций
CREATE TABLE payments.payment_transactions (
    transaction_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id BIGINT, -- Logical FK to order_db.orders(order_id)
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    amount NUMERIC(10, 2) NOT NULL CHECK (amount >= 0),
    currency VARCHAR(10) NOT NULL,
    status payment_status_enum NOT NULL DEFAULT 'PENDING',
    gateway_transaction_id VARCHAR(255) UNIQUE, -- ID от платежного провайдера
    payment_method_type payment_method_type_enum NOT NULL,
    error_message TEXT,
    processed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для сохраненных способов оплаты
CREATE TABLE payments.saved_payment_methods (
    method_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id)
    type VARCHAR(50) NOT NULL, -- e.g., 'CARD'
    token TEXT NOT NULL, -- Token from payment provider
    last_four_digits VARCHAR(4) NOT NULL,
    card_brand VARCHAR(50),
    expiry_month INTEGER,
    expiry_year INTEGER,
    is_default BOOLEAN DEFAULT FALSE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, token) -- Пользователь не может добавить один и тот же токен дважды
);

-- Таблица для запросов на выплату продавцу
CREATE TABLE payments.payout_requests (
    payout_id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
    amount NUMERIC(10, 2) NOT NULL CHECK (amount > 0),
    status payment_status_enum NOT NULL DEFAULT 'PENDING',
    processed_at TIMESTAMP WITH TIME ZONE,
    transaction_id VARCHAR(255), -- ID транзакции выплаты в банке/системе
    notes TEXT,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
