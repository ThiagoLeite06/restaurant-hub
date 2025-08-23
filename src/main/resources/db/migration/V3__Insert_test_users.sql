-- V3__Insert_test_users.sql

-- Inserir usuários de teste (um de cada tipo)
INSERT INTO users (name, email, login, password, address, user_type_id) VALUES 
(
    'João Silva', 
    'joao.owner@email.com', 
    'joao.owner', 
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', -- password: password
    'Rua das Flores, 123 - São Paulo, SP',
    (SELECT id FROM user_types WHERE name = 'OWNER')
),
(
    'Maria Santos', 
    'maria.client@email.com', 
    'maria.client', 
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', -- password: password
    'Av. Paulista, 456 - São Paulo, SP',
    (SELECT id FROM user_types WHERE name = 'CLIENT')
),
(
    'Admin Sistema', 
    'admin@restauranthub.com', 
    'admin', 
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', -- password: password
    'Centro Administrativo - São Paulo, SP',
    (SELECT id FROM user_types WHERE name = 'SYSTEM_ADMIN')
);