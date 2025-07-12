-- Database schema for Warehouse Management System (WMS)
-- This schema defines the structure for managing warehouses, locations, inventory items, stock movements, inbound shipments, and picking tasks.
-- This schema is designed to be used with PostgreSQL.
-- Enum for warehouse location types
CREATE TYPE wms.warehouse_location_type_enum AS ENUM (
    'AVAILABLE',
    'RESERVED',
    'QUARANTINE',
    'DAMAGED'
);
-- Enum for inventory item status
CREATE TYPE wms.inventory_item_status_enum AS ENUM (
    'AVAILABLE',
    'RESERVED',
    'QUARANTINE',
    'DAMAGED'
);
-- Enum for stock movement types
CREATE TYPE wms.stock_movement_type_enum AS ENUM (
    'RECEIVE',
    'PICK',
    'SHIP',
    'TRANSFER',
    'ADJUSTMENT_IN',
    'ADJUSTMENT_OUT'
);
-- Enum for inbound shipment status
CREATE TYPE wms.inbound_status_enum AS ENUM (
    'PLANNED',
    'IN_TRANSIT',
    'RECEIVED_PARTIAL',
    'RECEIVED_COMPLETED',
    'CANCELLED'
);
-- Enum for picking task status
CREATE TYPE wms.picking_task_status_enum AS ENUM (
    'NEW',
    'ASSIGNED',
    'IN_PROGRESS',
    'COMPLETED',
    'CANCELLED'
);
-- Enum for outbound shipment status
CREATE TYPE wms.outbound_status_enum AS ENUM (
    'PENDING_PICKING', --
    'READY_FOR_DISPATCH',
    'DISPATCHED',
    'DELIVERED'
);
--
CREATE TYPE wms.stocktake_status_enum AS ENUM (
    'PLANNED',
    'IN_PROGRESS',
    'COMPLETED',
    'CANCELLED'
);
-- Enum for
CREATE TYPE wms.stocktake_type_enum AS ENUM (
    'FULL',
    'CYCLE_COUNT'
);

CREATE TYPE wms.transfer_status_enum AS ENUM (
    'PLANNED', -- Planned transfer
    'IN_TRANSIT', -- Transfer is in progress
    'COMPLETED', -- Transfer completed
    'CANCELLED' -- Transfer was cancelled
);
-- Таблица для складов
CREATE TABLE wms.warehouses (
                            warehouse_id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255) UNIQUE NOT NULL,
                            address TEXT NOT NULL,
                            capacity_sq_m NUMERIC(10, 2),
                            is_active BOOLEAN DEFAULT TRUE
);

-- Таблица для мест хранения (ячеек)
CREATE TABLE wms.locations (
                           location_id BIGSERIAL PRIMARY KEY,
                           warehouse_id BIGINT NOT NULL,
                           code VARCHAR(100) NOT NULL, -- e.g., "A-01-01"
                           type wms.warehouse_location_type_enum NOT NULL,
                           max_capacity_units INTEGER,
                           is_active BOOLEAN DEFAULT TRUE,
                           UNIQUE (warehouse_id, code), -- Unique location code per warehouse
                           FOREIGN KEY (warehouse_id) REFERENCES wms.warehouses(warehouse_id) ON DELETE CASCADE
);

-- Таблица для складских позиций (инвентаря)
CREATE TABLE wms.inventory_items (
                                 inventory_item_id BIGSERIAL PRIMARY KEY,
                                 sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
                                 warehouse_id BIGINT NOT NULL,
                                 location_id BIGINT NOT NULL,
                                 quantity INTEGER NOT NULL CHECK (quantity >= 0),
                                 batch_number VARCHAR(255),
                                 expiration_date DATE,
                                 seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
                                 status wms.inventory_item_status_enum NOT NULL DEFAULT 'AVAILABLE',
                                 UNIQUE (sku_code, location_id, batch_number), -- A specific SKU/batch can only be in one location (or multiple if no batch)
                                 FOREIGN KEY (warehouse_id) REFERENCES wms.warehouses(warehouse_id) ON DELETE CASCADE,
                                 FOREIGN KEY (location_id) REFERENCES wms.locations(location_id) ON DELETE RESTRICT
);

-- Таблица для движений запасов
CREATE TABLE wms.stock_movements (
                                 movement_id BIGSERIAL PRIMARY KEY,
                                 inventory_item_id BIGINT, -- Optional: if specific item is tracked
                                 sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
                                 quantity INTEGER NOT NULL,
                                 from_location_id BIGINT,
                                 to_location_id BIGINT,
                                 movement_type wms.stock_movement_type_enum NOT NULL,
                                 reference_document_id BIGINT, -- e.g., order_id, inbound_id, stocktake_id
                                 reference_document_type VARCHAR(50), -- e.g., 'ORDER', 'INBOUND', 'STOCKTAKE'
                                 user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) or admin_db.admin_users(admin_id)
                                 moved_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (from_location_id) REFERENCES wms.locations(location_id) ON DELETE SET NULL,
                                 FOREIGN KEY (to_location_id) REFERENCES wms.locations(location_id) ON DELETE SET NULL
);

-- Таблица для входящих поставок
CREATE TABLE wms.inbound_shipments (
                                   inbound_id BIGSERIAL PRIMARY KEY,
                                   seller_id BIGINT NOT NULL, -- Logical FK to seller_db.sellers(seller_id)
                                   warehouse_id BIGINT NOT NULL,
                                   status wms.inbound_status_enum NOT NULL DEFAULT 'PLANNED',
                                   expected_arrival_date DATE NOT NULL,
                                   actual_arrival_date TIMESTAMP WITH TIME ZONE,
                                   notes TEXT,
                                   FOREIGN KEY (warehouse_id) REFERENCES wms.warehouses(warehouse_id) ON DELETE RESTRICT
);

-- Таблица для позиций входящей поставки
CREATE TABLE wms.inbound_items (
                               inbound_item_id BIGSERIAL PRIMARY KEY,
                               inbound_id BIGINT NOT NULL,
                               sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
                               expected_quantity INTEGER NOT NULL CHECK (expected_quantity > 0),
                               received_quantity INTEGER DEFAULT 0 CHECK (received_quantity >= 0),
                               FOREIGN KEY (inbound_id) REFERENCES wms.inbound_shipments(inbound_id) ON DELETE CASCADE
);

-- Таблица для заданий на отбор
CREATE TABLE wms.picking_tasks (
                               task_id BIGSERIAL PRIMARY KEY,
                               order_id BIGINT NOT NULL, -- Logical FK to order_db.orders(order_id)
                               warehouse_id BIGINT NOT NULL,
                               picker_user_id BIGINT, -- Logical FK to user_db.users(user_id) (WMS operator)
                               status wms.picking_task_status_enum NOT NULL DEFAULT 'NEW',
                               assigned_at TIMESTAMP WITH TIME ZONE,
                               completed_at TIMESTAMP WITH TIME ZONE,
                               FOREIGN KEY (warehouse_id) REFERENCES wms.warehouses(warehouse_id) ON DELETE RESTRICT
);

-- Таблица для позиций задания на отбор
CREATE TABLE wms.picking_task_items (
                                    task_item_id BIGSERIAL PRIMARY KEY,
                                    task_id BIGINT NOT NULL,
                                    sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
                                    quantity_to_pick INTEGER NOT NULL CHECK (quantity_to_pick > 0),
                                    picked_quantity INTEGER DEFAULT 0 CHECK (picked_quantity >= 0),
                                    source_location_id BIGINT, -- Recommended location for picking
                                    FOREIGN KEY (task_id) REFERENCES wms.picking_tasks(task_id) ON DELETE CASCADE,
                                    FOREIGN KEY (source_location_id) REFERENCES wms.locations(location_id) ON DELETE SET NULL
);

-- Таблица для исходящих отгрузок
CREATE TABLE wms.outbound_shipments (
                                    outbound_id BIGSERIAL PRIMARY KEY,
                                    order_id BIGINT NOT NULL, -- Logical FK to order_db.orders(order_id)
                                    warehouse_id BIGINT NOT NULL,
                                    status wms.outbound_status_enum NOT NULL DEFAULT 'PENDING_PICKING',
                                    carrier_id BIGINT, -- Logical FK to logistics_db.shipping_carriers(carrier_id)
                                    tracking_number VARCHAR(255),
                                    dispatch_at TIMESTAMP WITH TIME ZONE,
                                    FOREIGN KEY (warehouse_id) REFERENCES wms.warehouses(warehouse_id) ON DELETE RESTRICT
);

-- Таблица для инвентаризаций
CREATE TABLE wms.stocktakes (
                            stocktake_id BIGSERIAL PRIMARY KEY,
                            warehouse_id BIGINT NOT NULL,
                            type wms.stocktake_type_enum NOT NULL,
                            status wms.stocktake_status_enum NOT NULL DEFAULT 'PLANNED',
                            start_date DATE NOT NULL,
                            end_date DATE,
                            initiated_by_user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) (WMS operator)
                            FOREIGN KEY (warehouse_id) REFERENCES wms.warehouses(warehouse_id) ON DELETE RESTRICT
);

-- Таблица для позиций инвентаризации
CREATE TABLE wms.stocktake_items (
                                 stocktake_item_id BIGSERIAL PRIMARY KEY,
                                 stocktake_id BIGINT NOT NULL,
                                 sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
                                 location_id BIGINT,
                                 system_quantity INTEGER NOT NULL,
                                 counted_quantity INTEGER NOT NULL,
                                 discrepancy INTEGER NOT NULL, -- counted_quantity - system_quantity
                                 FOREIGN KEY (stocktake_id) REFERENCES wms.stocktakes(stocktake_id) ON DELETE CASCADE,
                                 FOREIGN KEY (location_id) REFERENCES wms.locations(location_id) ON DELETE SET NULL
);

-- Таблица для внутренних перемещений
CREATE TABLE wms.transfers (
                           transfer_id BIGSERIAL PRIMARY KEY,
                           sku_code VARCHAR(100) NOT NULL, -- Logical FK to product_db.product_variants(sku_code)
                           quantity INTEGER NOT NULL CHECK (quantity > 0),
                           from_location_id BIGINT NOT NULL,
                           to_location_id BIGINT NOT NULL,
                           status wms.transfer_status_enum NOT NULL DEFAULT 'PENDING',
                           initiated_by_user_id BIGINT NOT NULL, -- Logical FK to user_db.users(user_id) (WMS operator)
                           completed_at TIMESTAMP WITH TIME ZONE,
                           FOREIGN KEY (from_location_id) REFERENCES wms.locations(location_id) ON DELETE RESTRICT,
                           FOREIGN KEY (to_location_id) REFERENCES wms.locations(location_id) ON DELETE RESTRICT
);

