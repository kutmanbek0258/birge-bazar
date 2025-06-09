CREATE TABLE users.addresses (
   id BIGSERIAL PRIMARY KEY,
   user_id BIGINT NOT NULL,
   address_text VARCHAR(1024),
   city VARCHAR(128),
   postal_code VARCHAR(32),
   is_default BOOLEAN DEFAULT FALSE
);
