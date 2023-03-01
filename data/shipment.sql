-- shipment history
CREATE TABLE order_history (
    id bigserial PRIMARY KEY,
    item_id bigint NOT NULL FOREIGN KEY REFERENCES inventory_items (item_id),
    shipment_id int NOT NULL,
    shipment_date timestamp NOT NULL,
    item_name varchar(50) NOT NULL,
    quantity int NOT NULL,
    price numeric(10,2) NOT NULL,
    total_price numeric(10,2) NOT NULL
);

-- order history
CREATE TABLE order_history (
    id bigserial PRIMARY KEY,
    shipment_id int NOT NULL,
    order_date timestamp NOT NULL,
    item_id bigint NOT NULL FOREIGN KEY REFERENCES menu_items (item_id),
    price numeric(10,2) NOT NULL,
    payment_method varchar(50) NOT NULL,
    quantity int NOT NULL
);