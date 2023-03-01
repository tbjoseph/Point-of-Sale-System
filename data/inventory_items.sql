CREATE TABLE inventory_items (
    inventory_id BIGINT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL,
    shipment_size INTEGER NOT NULL
);