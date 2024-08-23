## Overview
This repository contains the code for a newly developed Point of Sale (POS) system designed for Panda Express to address the inefficiencies during peak demand periods, primarily caused by the bottleneck at the checkout station. The new POS system enhances the speed and efficiency of the checkout process, benefiting servers and managers by streamlining customer interactions.
## Features
* Streamlined Checkout Interface: Simplifies the checkout process to match the pace of the serving line, significantly reducing wait times.
* Order Entry and Payment Processing: Enables quick entry of orders and processing of payments to expedite service.
Historical Data Visualization: Managers can view transaction histories, displayed graphically, to track item usage and sales trends.
* Inventory Management: Tracks inventory items, their quantities, and the rate at which they are being used, * facilitating timely reordering based on actual sales data.
* Order History Tracking: Stores details of all transactions including date, menu items, prices, and payment methods for future reference and inventory management.
* Shipment History Analysis: Helps in understanding shipment trends and managing supply chain costs by recording details of past shipments.
* Employee Management: Manages employee data including work hours, roles, and access privileges within the system.

## Technical Specifications
* Frontend: Developed using FXML with JavaFX for dynamic, responsive user interfaces.
* Backend: Java backend interacting with a PostgreSQL database through java.sql for robust data handling.
* Database Design: Utilizes a relational database schema that categorically separates inventory, menu, orders, shipments, and employee data for comprehensive management.
* Security: Implements role-based access control with specific functionalities available to managers and servers to maintain system integrity and data privacy.

## System Operations
* Authentication:
  * Login and user session management without privileges.
  * Manager-level user creation with elevated privileges.
* Ordering System:
  * Retrieve menu items for display on UI.
  * Place orders and associate them with menu items.
* Managerial Functions:
  * Access and update inventory and menu items.
  * Manage associations between menu items and inventory.
  * Generate reports on orders and shipments.

## Development and Deployment
* Backend DAO Class: Serves as the abstraction layer interfacing with the database, ensuring clean separation between the database operations and the user interface logic.
* User Interface: Data interaction facilitated by backend functions exposed through a cleanly designed frontend, improving usability and accessibility for end-users.

## Configuration

This project uses Gradle to manage dependencies. To build and run, you can either use the scripts:

```bash
# if linux or mac
./gradlew build # or run, etc.
# if windows
.\gradlew.bat build # or run, etc.
```

Or you can use the VSCode extension. Open the Gradle tab, and select `app/Tasks/application/run` for running or `app/Tasks/build/build` for building.

To login to the database, the environment variables `PSQL_USER` and `PSQL_PASS` are used to determine the username and password. If these are not set, an error will be visible on the login page, and it will not be possible to log in to the system.

## Reference

- [JavaFX](https://openjfx.io/index.html)
- [PostgreSQL](https://jdbc.postgresql.org/documentation/)
