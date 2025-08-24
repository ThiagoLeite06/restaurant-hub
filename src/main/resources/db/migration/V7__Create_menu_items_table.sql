-- Create menu_items table
CREATE TABLE menu_items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price > 0),
    available_only_in_restaurant BOOLEAN NOT NULL DEFAULT FALSE,
    photo_path VARCHAR(500),
    restaurant_id BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT fk_menu_items_restaurant
        FOREIGN KEY (restaurant_id) 
        REFERENCES restaurants (id) 
        ON DELETE CASCADE,
        
    CONSTRAINT uk_menu_item_name_restaurant 
        UNIQUE (name, restaurant_id)
);

-- Create indexes for better performance
CREATE INDEX idx_menu_items_restaurant_id ON menu_items(restaurant_id);
CREATE INDEX idx_menu_items_active ON menu_items(active);
CREATE INDEX idx_menu_items_price ON menu_items(price);