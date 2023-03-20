CREATE TABLE menu_inventory_assoc (
    id bigserial PRIMARY KEY,
    menu_item_id bigint NOT NULL REFERENCES menu_items (id),
    inventory_item_id bigint NOT NULL REFERENCES inventory_items (inventory_id)
);