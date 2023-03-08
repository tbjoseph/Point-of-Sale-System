CREATE TABLE menu_items (
    id bigserial PRIMARY KEY,
    name varchar(50),
    meal_type varchar(50) NOT NULL,
    description varchar(150) NOT NULL,
    price_small numeric(10,2),
    price_med numeric(10,2),
    price_large numeric(10,2)
);