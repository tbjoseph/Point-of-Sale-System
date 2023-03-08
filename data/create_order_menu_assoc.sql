CREATE TABLE order_menu_assoc (
    order_id bigint NOT NULL REFERENCES order_history (id),
    menu_item_id bigint NOT NULL REFERENCES menu_items (id),
    quantity int NOT NULL
);