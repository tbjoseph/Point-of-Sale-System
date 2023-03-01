-- employees table
CREATE TABLE employees (
    id bigint PRIMARY KEY,
    name varchar(255) NOT NULL,
    gender varchar(255) NOT NULL,
    birth_date date NOT NULL,
    permission varchar(255) NOT NULL CHECK (permission IN ('employee', 'manager'))
);
