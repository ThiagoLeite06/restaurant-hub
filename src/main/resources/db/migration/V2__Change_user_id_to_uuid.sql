-- Migration to change user ID from BIGSERIAL to UUID
-- This maintains data while changing the structure

-- Create temporary table with UUID structure
CREATE TABLE users_temp (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    address VARCHAR(500),
    user_type VARCHAR(50) DEFAULT 'CUSTOMER',
    enabled BOOLEAN DEFAULT TRUE,
    account_non_expired BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE
);

-- Copy existing data, generating new UUIDs
INSERT INTO users_temp (name, email, login, password, created_at, updated_at, address, user_type, enabled, account_non_expired, account_non_locked, credentials_non_expired)
SELECT name, email, login, password, created_at, updated_at, address, user_type, enabled, account_non_expired, account_non_locked, credentials_non_expired
FROM users;

-- Drop old table and rename temp table
DROP TABLE users;
ALTER TABLE users_temp RENAME TO users;