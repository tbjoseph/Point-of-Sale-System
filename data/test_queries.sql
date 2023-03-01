-- test queries to be run against the database

-- Orders

-- 1. List 10 of the orders that happened on July 5th
SELECT * FROM order_history WHERE order_date LIKE '2022-07-05%' LIMIT 10;

-- 2. Insert a new order with one item - a drink
INSERT INTO order_history (order_id, order_date, item_id, price, payment_method, quantity) VALUES (129532, '2023-02-28 12:00:00', 22, 2.00, 'dining_dollars', 1);

-- 3. Find the total value of sales on May 4th
WITH order_totals AS (
    SELECT order_id, SUM(price) AS total
    FROM order_history
    GROUP BY order_id
) SELECT SUM(total) FROM order_totals WHERE order_date LIKE '2022-05-04%';

-- Employees
-- List all employees
SELECT * FROM employees WHERE permission = 'employee';

--List all managers
SELECT * FROM employees WHERE permission = 'manager';

-- Change an employee's name and gender
UPDATE employees SET name = 'Jane Smith' SET gender = 'Female' WHERE id = 111222333;

-- Add a newly hired employee into the database
INSERT INTO employees (id, name, gender, birth_date, permission) values (000000001, 'Daxton Gilliam', 'Male', '1997-01-01', 'employee');


-- Menu
SELECT * FROM menu_items WHERE meal_type = 'entree'; -- selecting all the entrees from the menu
SELECT * FROM menu_items WHERE name LIKE '%The Original Orange Chicken%'; -- selecting a specific menu name from the menu
SELECT * FROM menu_items WHERE description LIKE '%mushrooms%'; -- selecting anything with mushroom in the description

-- Inventory
SELECT * FROM inventory_items WHERE item_name LIKE '%Rice%';     -- List all rices
SELECT * FROM inventory_items WHERE item_name LIKE '%Sauce%';    -- List all sauces
SELECT * FROM inventory_items WHERE quantity = 0;                -- List all unavailable items
UPDATE inventory_items SET quantity = 10 WHERE inventory_id = 0; -- Update the breaded chicken quantity to 10

-- Shipments

-- Find total cost of all shipments
SELECT SUM(total_price) from shipment_history;

-- List all shipments of Teriyaki Sauce
SELECT * FROM shipment_history WHERE item_name = 'Teriyaki Sauce';