-- V5__Create_restaurants_table.sql

CREATE TABLE restaurants (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(500) NOT NULL,
    cuisine_type VARCHAR(50) NOT NULL,
    opening_hours VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Inserir alguns restaurantes de teste
INSERT INTO restaurants (name, address, cuisine_type, opening_hours) VALUES 
('Restaurante Árabe Al-Andaluz', 'Rua da Liberdade, 123 - São Paulo, SP', 'ARABE', 'Seg-Dom: 11h às 23h'),
('Churrascaria Brasileira', 'Av. Paulista, 456 - São Paulo, SP', 'BRASILEIRA', 'Ter-Dom: 12h às 00h'),
('Sushi House', 'Rua Galvão Bueno, 789 - São Paulo, SP', 'JAPONESA', 'Seg-Sáb: 18h às 01h'),
('Cantina Italiana', 'Rua Bela Cintra, 321 - São Paulo, SP', 'ITALIANA', 'Qua-Dom: 19h às 23h'),
('Casa Portuguesa', 'Rua do Ouvidor, 654 - São Paulo, SP', 'PORTUGUESA', 'Qui-Dom: 17h às 22h');