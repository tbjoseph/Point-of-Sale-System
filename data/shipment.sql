-- shipment history
CREATE TABLE shipment_history (
    id bigserial PRIMARY KEY,
    item_id bigint NOT NULL FOREIGN KEY REFERENCES inventory_items (item_id),
    shipment_id int NOT NULL,
    shipment_date timestamp NOT NULL,
    item_name varchar(50) NOT NULL,
    quantity int NOT NULL,
    price numeric(10,2) NOT NULL,
    total_price numeric(10,2) NOT NULL
);