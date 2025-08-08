--liquibase formatted sql

--changeset sellers:tables-1

-- Таблица для продавцов
CREATE TABLE sellers.sellers (
    seller_id BIGSERIAL PRIMARY KEY,
    user_id UUID UNIQUE NOT NULL, -- Logical FK to user_db.users(user_id) - owner of the seller account
    shop_name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    logo_url TEXT,
    status seller_status_enum NOT NULL DEFAULT 'PENDING_APPROVAL',
    commission_rate NUMERIC(5, 4) NOT NULL CHECK (commission_rate >= 0 AND commission_rate <= 1), -- e.g., 0.15 for 15%
    contact_email VARCHAR(255) NOT NULL,
    contact_phone VARCHAR(50),
    bank_details_json JSONB, -- Encrypted or tokenized bank details
    approval_date TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для сотрудников продавца
CREATE TABLE sellers.seller_staff (
    staff_id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    user_id UUID UNIQUE NOT NULL, -- Logical FK to user_db.users(user_id) - staff member's user account
    role seller_staff_role_enum NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    accepted_at TIMESTAMP WITH TIME ZONE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES sellers.sellers(seller_id) ON DELETE CASCADE
);
