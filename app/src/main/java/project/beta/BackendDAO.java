package project.beta;

import project.beta.manager.ManagerController.Menu;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// import javafx.event.ActionEvent;
// import javafx.scene.control.Menu;

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
     * @throws SQLException
     */
    public boolean login(String username, String password) {
        // TODO: implement this
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
     * Try to login with the form data
     * 
     * @param event - the event that triggered this method
     * @throws IOException
     * @throws SQLException
     */
    public void addMenuItem(Integer menuIdString, String menuNameString, Integer inventoryIdField,
            String mealTypeField, String descriptionField) throws IOException, SQLException {
        try {
            String query = "INSERT INTO menu_items (menu_id, name, inventory_id, meal_type, description) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, menuIdString);
            stmt.setString(2, menuNameString);
            stmt.setInt(3, inventoryIdField);
            stmt.setString(4, mealTypeField);
            stmt.setString(5, descriptionField);

            // Execute the statement and update the table
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMenu(Menu menu) {
        try {
            // Create an SQL statement to update the data
            String query = "UPDATE menu_items SET name=?, inventory_id=?, meal_type=?, description=? WHERE menu_id=?";

            // Prepare the statement and set the parameters
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, menu.getName());
            stmt.setInt(2, menu.getInventoryId());
            stmt.setString(3, menu.getMealType());
            stmt.setString(4, menu.getDescription());
            stmt.setInt(5, menu.getMenuId());

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

}
