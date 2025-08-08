--liquibase formatted sql

--changeset reviews:update-reviews-table-for-mvp-1

-- 1. Rename the primary key column
ALTER TABLE reviews.reviews RENAME COLUMN review_id TO id;

-- 2. Drop unnecessary columns
ALTER TABLE reviews.reviews DROP COLUMN IF EXISTS order_id;
ALTER TABLE reviews.reviews DROP COLUMN IF EXISTS is_anonymous;
ALTER TABLE reviews.reviews DROP COLUMN IF EXISTS status;

-- 3. Drop audit columns that are now handled by Hibernate's @CreationTimestamp and @UpdateTimestamp
ALTER TABLE reviews.reviews DROP COLUMN IF EXISTS created_by;
ALTER TABLE reviews.reviews DROP COLUMN IF EXISTS last_modified_by;

-- 4. Rename timestamp columns to match the simplified entity
ALTER TABLE reviews.reviews RENAME COLUMN created_date TO created_at;
ALTER TABLE reviews.reviews RENAME COLUMN last_modified_date TO updated_at;

-- 5. Drop the now unused enum type
DROP TYPE IF EXISTS reviews.review_status_enum;
