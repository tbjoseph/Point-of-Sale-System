-- order history
CREATE TABLE order_history (
    id bigserial PRIMARY KEY,
    order_date timestamp NOT NULL,
    price numeric(10,2) NOT NULL,
    payment_method varchar(50) NOT NULL
);
