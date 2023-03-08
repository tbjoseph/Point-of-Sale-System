CREATE TABLE menu_inventory_assoc (
    menu_item_id bigint NOT NULL REFERENCES menu_items (id),
    inventory_item_id bigint NOT NULL REFERENCES inventory_items (inventory_id)
);