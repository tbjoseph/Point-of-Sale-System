-- shipment history
CREATE TABLE shipment_history (
    shipment_id bigserial PRIMARY KEY,
    inventory_id bigint NOT NULL REFERENCES inventory_items (inventory_id),
    shipment_date timestamp NOT NULL,
    item_name varchar(50) NOT NULL,
    quantity int NOT NULL,
    price numeric(10,2) NOT NULL,
    total_price numeric(10,2) NOT NULL
);