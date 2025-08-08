--liquibase formatted sql

--changeset logistics:tables-1

-- Таблица для служб доставки
CREATE TABLE logistics.shipping_carriers (
    carrier_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    tracking_url_pattern TEXT,
    api_key_encrypted TEXT, -- Encrypted API key for integration
    is_active BOOLEAN DEFAULT TRUE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Таблица для способов доставки
CREATE TABLE logistics.shipping_methods (
    method_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    base_cost NUMERIC(10, 2) NOT NULL CHECK (base_cost >= 0),
    min_delivery_days INTEGER NOT NULL CHECK (min_delivery_days >= 0),
    max_delivery_days INTEGER NOT NULL CHECK (max_delivery_days >= min_delivery_days),
    is_active BOOLEAN DEFAULT TRUE,
    carrier_id BIGINT,
    is_marketplace_delivery BOOLEAN DEFAULT FALSE, -- True if handled by marketplace's WMS/logistics
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (carrier_id) REFERENCES logistics.shipping_carriers(carrier_id) ON DELETE SET NULL
);

-- Таблица для пунктов выдачи
CREATE TABLE logistics.pickup_points (
    point_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    latitude NUMERIC(10, 7) NOT NULL,
    longitude NUMERIC(10, 7) NOT NULL,
    working_hours_json JSONB DEFAULT '{}', -- e.g., {"Mon": "9:00-18:00", "Tue": "9:00-18:00"}
    is_active BOOLEAN DEFAULT TRUE,
    created_by VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(255),
    last_modified_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
