CREATE TABLE menu_items (
    id bigserial PRIMARY KEY,
    menu_id int NOT NULL,
    name varchar(50),
    inventory_id bigint NOT NULL FOREIGN KEY REFERENCES inventory_items (inventory_id),
    type varchar(50) NOT NULL,
    description varchar(150) NOT NULL
);