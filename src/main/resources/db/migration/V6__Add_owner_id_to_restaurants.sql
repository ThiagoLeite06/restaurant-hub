-- V6__Add_owner_id_to_restaurants.sql

-- Adicionar coluna owner_id na tabela restaurants
ALTER TABLE restaurants 
ADD COLUMN owner_id UUID;

-- Criar constraint de foreign key
ALTER TABLE restaurants 
ADD CONSTRAINT fk_restaurants_owner 
FOREIGN KEY (owner_id) REFERENCES users(id);

-- Atualizar restaurantes existentes com owners do tipo OWNER
-- Vamos associar cada restaurante com um usuário OWNER existente
UPDATE restaurants r
SET owner_id = (
    SELECT u.id 
    FROM users u 
    JOIN user_types ut ON u.user_type_id = ut.id 
    WHERE ut.name = 'OWNER'
    LIMIT 1
)
WHERE r.owner_id IS NULL;

-- Fazer a coluna NOT NULL após popular os dados
ALTER TABLE restaurants 
ALTER COLUMN owner_id SET NOT NULL;