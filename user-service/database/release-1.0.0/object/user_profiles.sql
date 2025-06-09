CREATE TABLE users.user_profiles (
   id BIGSERIAL PRIMARY KEY,
   user_id BIGINT UNIQUE NOT NULL, -- связь с Auth Service
   full_name VARCHAR(255),
   phone VARCHAR(32),
   email VARCHAR(255),
   avatar_url VARCHAR(512),
   default_address_id BIGINT
);