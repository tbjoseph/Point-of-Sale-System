package project.beta;

import project.beta.manager.ManagerController.Inventory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import project.beta.types.MenuItem;

/**
 * BackendDAO is a class that handles all communication with the database.
 * 
 * @author Griffith Thomas
 */
public class BackendDAO {
    private Connection connection;

    /**
     * Default constructor for BackendDAO.
     * Requires the environment variables PSQL_USER and PSQL_PASS to be set to
     * login.
     */
    public BackendDAO() {
        String username = System.getenv("PSQL_USER");
        String password = System.getenv("PSQL_PASS");

        // if we don't have the environment variables, don't try to connect
        if (username == null || password == null) {
            System.err.println("PSQL_USER and PSQL_PASS environment variables must be set");
            connection = null;
            return;
        }

        // create connection
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_beta",
                    username, password);
            // connection = null;
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    /**
     * Submits an order and adds it to the database
     * 
     * @param paymentMethod the method of payment
     * @param date          the date of the order
     * @param price         the price of the order
     */
    public void submitOrder(String paymentMethod, LocalDateTime date, float price) {
        try {
            String query = "INSERT INTO order_history (order_date, price, payment_method) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            Timestamp timestamp = Timestamp.valueOf(date);
            stmt.setTimestamp(1, timestamp);
            stmt.setFloat(2, price);
            stmt.setString(3, paymentMethod);
            // Execute the statement and update the table
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the username and password are valid
     * 
     * @param username username to check
     * @param password password to check
     * @return true if the username and password are valid, false otherwise
     */
    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        try {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT permission FROM Employees WHERE name = ?");
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
        return false;
    }

    /**
     * Gets the permissions of the user
     * 
     * @param username username to check
     * @return the permissions of the user
     * @throws SQLException if the query fails
     */
    public String getPermission(String username) throws SQLException {
        // query to back end with username to get the permissions.
        PreparedStatement statement = connection.prepareStatement("SELECT permission FROM Employees WHERE name = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            return rs.getString("permission");
        }
        return null;
    }

    /**
     * Gets the inventory items from the database
     * 
     * @param menuNameString   name of the menu item
     * @param mealTypeField    meal type of the menu item
     * @param descriptionField description of the menu item
     * @param price_small      price of the small menu item
     * @param price_med        price of the medium menu item
     * @param price_large      price of the large menu item
     */
    public void addMenuItem(String menuNameString, String mealTypeField, String descriptionField, Float price_small,
            Float price_med, Float price_large) {
        try {
            String query = "INSERT INTO menu_items (name, meal_type, description, price_small, price_med, price_large) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, menuNameString);
            stmt.setString(2, mealTypeField);
            stmt.setString(3, descriptionField);
            stmt.setFloat(4, price_small);
            stmt.setFloat(5, price_med);
            stmt.setFloat(6, price_large);
            // Execute the statement and update the table
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the inventory items from the database
     * 
     * @param menu menu item to update
     */
    public void updateMenu(MenuItem menu) {
        try {
            // Create an SQL statement to update the data
            String query = "UPDATE menu_items SET name=?, meal_type=?, description=?, price_small=?, price_med=?, price_large=? WHERE id = ?";

            // Prepare the statement and set the parameters
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, menu.name);
            stmt.setString(2, menu.mealType);
            stmt.setString(3, menu.description);
            stmt.setFloat(4, menu.priceSmall);
            stmt.setFloat(5, menu.priceMedium);
            stmt.setFloat(6, menu.priceLarge);
            stmt.setLong(7, menu.getIndex());
            // Execute the statement and check the number of rows affected
            int rows = stmt.executeUpdate();
            if (rows == 1) {
                System.out.println("Menu item updated successfully");
            } else {
                System.out.println("Failed to update menu item");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the inventory items from the database
     * 
     * @param inventory inventory item to update
     */
    public void updateInventory(Inventory inventory) {
        try {
            // Create an SQL statement to update the data
            String query = "UPDATE inventory_items SET item_name=?, quantity=?, shipment_size=? WHERE inventory_id = ?";

            // Prepare the statement and set the parameters
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, inventory.itemName);
            stmt.setInt(2, inventory.quantity);
            stmt.setInt(3, inventory.shipmentSize);
            stmt.setLong(4, inventory.inventoryId);
            // Execute the statement and check the number of rows affected
            int rows = stmt.executeUpdate();
            if (rows == 1) {
                System.out.println("Inventory item updated successfully");
            } else {
                System.out.println("Failed to update inventory item");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the menu items from the database
     * 
     * @return the menu items from the database
     * @throws SQLException if the query fails
     */
    public ArrayList<MenuItem> getMenuItems() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM menu_items");
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        while (rs.next()) {
            MenuItem menuItem = new MenuItem(rs.getLong("id"), rs.getString("name"), rs.getString("meal_type"),
                    rs.getString("description"), rs.getFloat("price_small"), rs.getFloat("price_med"),
                    rs.getFloat("price_large"));
            menuItems.add(menuItem);
        }
        return menuItems;
    }

    /**
     * Gets the inventory items from the database
     * 
     * @return the inventory items from the database
     * @throws SQLException if the query fails
     */
    public ResultSet getInventoryItems() throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery("SELECT * FROM inventory_items");
    }
}
