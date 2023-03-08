package project.beta;

import project.beta.manager.ManagerController.Inventory;
import project.beta.manager.ManagerController.Menu;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    /**
     * Executes a query and returns the ResultSet
     * 
     * @param query - query to execute
     * @return ResultSet of the query
     */
    public ResultSet executeQuery(String query) {
        try {
            java.sql.Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    /**
     * Checks if the username and password are valid
     * 
     * @param username - username to check
     * @param password - password to check
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
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
            System.out.println(rs);
            while (rs.next()) {
                System.out.println(rs.getString("permission"));
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
     * @param username - username to check
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
     * @param menuNameString   - name of the menu item
     * @param mealTypeField    - meal type of the menu item
     * @param descriptionField - description of the menu item
     * @param price_small      - price of the small menu item
     * @param price_med        - price of the medium menu item
     * @param price_large      - price of the large menu item
     * @throws SQLException if the query fails
     */
    public void addMenuItem(String menuNameString, String mealTypeField, String descriptionField, Float price_small,
            Float price_med, Float price_large) throws SQLException {
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
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the inventory items from the database
     * 
     * @param menu - menu item to update
     */
    public void updateMenu(Menu menu) {
        try {
            // Create an SQL statement to update the data
            String query = "UPDATE menu_items SET name=?, meal_type=?, description=?, price_small=?, price_med=?, price_large=? WHERE id = ?";

            // Prepare the statement and set the parameters
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, menu.getName());
            stmt.setString(2, menu.getMealType());
            stmt.setString(3, menu.getDescription());
            stmt.setFloat(4, menu.getPriceSmall());
            stmt.setFloat(5, menu.getPriceMedium());
            stmt.setFloat(6, menu.getPriceLarge());
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
     * @param inventory - inventory item to update
     */
    public void updateInventory(Inventory inventory) {
        try {
            // Create an SQL statement to update the data
            String query = "UPDATE inventory_items SET item_name=?, quantity=?, shipment_size=? WHERE inventory_id = ?";

            // Prepare the statement and set the parameters
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, inventory.getItemName());
            stmt.setInt(2, inventory.getQuantity());
            stmt.setInt(3, inventory.getShipmentSize());
            stmt.setLong(4, inventory.getInventoryId());
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

}
