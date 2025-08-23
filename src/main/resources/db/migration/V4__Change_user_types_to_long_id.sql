-- V4__Change_user_types_to_long_id.sql

-- Criar nova tabela user_types com ID BIGINT
DROP TABLE IF EXISTS user_types CASCADE;

CREATE TABLE user_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Inserir tipos de usuário padrão
INSERT INTO user_types (id, name, description) VALUES 
(1, 'OWNER', 'Proprietário de restaurante'),
(2, 'CLIENT', 'Cliente usuário do sistema'),
(3, 'SYSTEM_ADMIN', 'Administrador do Sistema');

-- Alterar tabela users para usar BIGINT em user_type_id
ALTER TABLE users DROP CONSTRAINT IF EXISTS fk_users_user_type;
ALTER TABLE users DROP COLUMN user_type_id;
ALTER TABLE users ADD COLUMN user_type_id BIGINT NOT NULL DEFAULT 2;

-- Criar constraint foreign key
ALTER TABLE users ADD CONSTRAINT fk_users_user_type 
    FOREIGN KEY (user_type_id) REFERENCES user_types(id);

-- Atualizar usuários existentes com tipos apropriados
UPDATE users SET user_type_id = 1 WHERE login = 'joao.owner';
UPDATE users SET user_type_id = 2 WHERE login = 'maria.client'; 
UPDATE users SET user_type_id = 3 WHERE login = 'admin';