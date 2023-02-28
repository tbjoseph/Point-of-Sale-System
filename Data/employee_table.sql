-- order history
CREATE TABLE employees (
    id bigserial PRIMARY KEY,
    order_id int NOT NULL,
    order_date timestamp NOT NULL,
    item_id bigint NOT NULL FOREIGN KEY REFERENCES menu_items (item_id),
    price numeric(10,2) NOT NULL,
    payment_method varchar(50) NOT NULL,
    quantity int NOT NULL
);
