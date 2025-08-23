-- V1__Create_user_types_table.sql

CREATE TABLE user_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Inserir tipos de usuário padrão
INSERT INTO user_types (name, description) VALUES 
('OWNER', 'Proprietário de restaurante'),
('CLIENT', 'Cliente usuário do sistema'),
('SYSTEM_ADMIN', 'Administrador do Sistema');