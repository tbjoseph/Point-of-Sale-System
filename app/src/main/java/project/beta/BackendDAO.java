package project.beta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Pair;

import javax.naming.spi.DirStateFactory.Result;

import project.beta.types.InventoryItem;
import project.beta.types.MenuItem;
import project.beta.types.Association;
import project.beta.types.OrderView;
import project.beta.types.OrderItem;

/**
 * BackendDAO is a data access object (DAO) that handles all communication with
 * the database. The DAO is a singleton, so only one instance of the DAO should
 * exist at a time. It uses the environment variables PSQL_USER and PSQL_PASS to
 * login to the database, and will throw an error if they are not set.
 * 
 * Errors are propagated to the caller, so that they can be handled by the UI.
 * 
 * @author Griffith Thomas
 */
public class BackendDAO {
    private Connection connection;

    private HashMap<Long, ArrayList<Long>> menu_inventory_assoc;
    private HashMap<Pair<String, Long>, Long> inventory_data;

    /**
     * Default constructor for BackendDAO. Requires the environment variables
     * PSQL_USER and PSQL_PASS to be set to login.
     * 
     * @throws SQLException     if the connection to the database fails
     * @throws RuntimeException if the environment variables are not set
     */
    public BackendDAO() throws SQLException {
        String username = System.getenv("PSQL_USER");
        String password = System.getenv("PSQL_PASS");

        // if we don't have the environment variables, don't try to connect
        if (username == null || password == null) {
            throw new RuntimeException("PSQL_USER and PSQL_PASS environment variables must be set");
        }

        // create connection
        connection = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_beta",
                username, password);

        // menu_inventory_assoc = null;
    }

    /**
     * Submits an order and adds it to the database
     * 
     * @param paymentMethod the method of payment
     * @param date          the date of the order
     * @param price         the price of the order
     * @param orders        the OrderView object
     * 
     * @throws SQLException     if the query fails
     * @throws RuntimeException if the order is empty
     */
    public void submitOrder(String paymentMethod, LocalDateTime date, float price, OrderView orders)
            throws SQLException, RuntimeException {
        if (orders.getOrderItems().size() == 0) {
            throw new RuntimeException("Cannot submit an empty order");
        }
        String query = "INSERT INTO order_history (order_date, price, payment_method) VALUES (?, ?, ?) RETURNING id";
        PreparedStatement stmt = connection.prepareStatement(query);
        Timestamp timestamp = Timestamp.valueOf(date);
        stmt.setTimestamp(1, timestamp);
        stmt.setFloat(2, price);
        stmt.setString(3, paymentMethod);
        // Execute the statement and update the table
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Long orderID = rs.getLong("id");
            update_order_menu_assoc(orderID, orders);
            return;
        }
    }

    /**
     * Adds order and menu items to order_menu_assoc
     * 
     * @param orderID the ID of the order
     * @param orders  the OrderView object
     * 
     * @throws SQLException if the query fails
     */
    public void update_order_menu_assoc(long orderID, OrderView orders) throws SQLException {
        HashMap<Long, Long> order_menu_assoc = new HashMap<>();
        for (OrderItem currentItem : orders.getOrderItems()) {
            for (MenuItem currMenuItem : currentItem.menuItems) {
                if (order_menu_assoc.containsKey(currMenuItem.getIndex())) {
                    order_menu_assoc.put(currMenuItem.getIndex(), order_menu_assoc.get(currMenuItem.getIndex()) + 1);
                } else {
                    order_menu_assoc.put(currMenuItem.getIndex(), 1L);
                }
            }
        }

        String values = "";

        for (Long menuItemID : order_menu_assoc.keySet()) {
            Long quantity = order_menu_assoc.get(menuItemID);
            values += "(" + orderID + ", " + menuItemID + ", " + quantity + "), ";
        }

        values = values.substring(0, values.length() - 2);

        String query = "INSERT INTO order_menu_assoc (order_id, menu_item_id, quantity) VALUES " + values;

        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    /**
     * Decreases inventory by the amount of items in orders
     * 
     * @param orders the OrderView object
     * 
     * @throws SQLException if the query fails
     */
    public void decreaseInventory(OrderView orders) throws SQLException {

        /*
         * - Initialize list of all Inventory Ids
         * - Iterate through each OrderItem in the OrderView
         * - Iterate through each MenuItem in the OrderItem
         * - append the MenuItem's list of inventory IDs from the hash to the running
         * list inventoryOrderIDs
         */

        ArrayList<Long> inventoryOrderIDs = new ArrayList<>();

        for (OrderItem currentOrder : orders.getOrderItems()) {

            for (MenuItem currMenuItem : currentOrder.menuItems) {

                ArrayList<Long> arr = menu_inventory_assoc.get(currMenuItem.getIndex());
                inventoryOrderIDs.addAll(arr);

            }

        }

        // - SQL query to decrease quantity of respective inventory_id each time the
        // inventory_id appears in Inventory Id. Should be something like this:
        // - UPDATE inventory_items SET quantity = quantity - 1 WHERE inventory_id IN (
        // {ArrayList of inventory ids} )

        for (Long id : inventoryOrderIDs) {
            // Create a PreparedStatement to execute an update query
            String updateQuery = "UPDATE inventory_items SET quantity = quantity - 1 WHERE inventory_id = ?";
            PreparedStatement stmt = connection.prepareStatement(updateQuery);
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Constructs hash map for menu_inventory_assoc
     * 
     * @throws SQLException if the query fails
     */
    public void construct_menu_inventory_assoc() throws SQLException {
        if (menu_inventory_assoc != null)
            return;

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM menu_inventory_assoc ORDER BY menu_item_id");
        menu_inventory_assoc = new HashMap<>();

        while (rs.next()) {
            Long menu_item_id = rs.getLong("menu_item_id");
            Long inventory_item_id = rs.getLong("inventory_item_id");

            if (menu_inventory_assoc.containsKey(menu_item_id)) {
                // If the key is already present in the HashMap, retrieve the ArrayList and add
                // the value to it
                ArrayList<Long> inventory_item_id_list = menu_inventory_assoc.get(menu_item_id);
                inventory_item_id_list.add(inventory_item_id);
            } else {
                // If the key is not present in the HashMap, create a new ArrayList, add the
                // value to it, and put it in the HashMap
                ArrayList<Long> inventory_item_id_list = new ArrayList<>();
                inventory_item_id_list.add(inventory_item_id);
                menu_inventory_assoc.put(menu_item_id, inventory_item_id_list);
            }
        }
    }

    /**
     * Constructs hash map for inventory_data
     * 
     * @throws SQLException if the query fails
     */
    public HashMap<Pair<String, Long>, Long> construct_inventory_data(Timestamp timestamp) throws SQLException {

        // Initialize all inventory items to have been a part of 0 orders
        Statement stmt0 = connection.createStatement();
        ResultSet rs0 = stmt0.executeQuery("SELECT item_name,quantity FROM inventory_items");
        inventory_data = new HashMap<>();

        while (rs0.next()) {
            String item_name = rs0.getString("item_name");
            Long quantity = rs0.getLong("quantity");

            Pair<String, Long> pair = new Pair<>(item_name, quantity); // name and remaining stock for a given item

            inventory_data.put(pair, 0L);
        }

        // Record number of item instances in order history
        String query = "SELECT i.item_name,i.quantity FROM order_history o JOIN order_menu_assoc a ON a.order_id = o.id JOIN menu_items m ON m.id = a.menu_item_id JOIN menu_inventory_assoc b ON b.menu_item_id = m.id JOIN inventory_items i ON i.inventory_id = b.inventory_item_id WHERE o.order_date > ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setTimestamp(1, timestamp);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String item_name = rs.getString("item_name");
            Long quantity = rs.getLong("quantity");

            Pair<String, Long> pair = new Pair<>(item_name, quantity); // name and remaining stock for a given item

            Long inventory_sold = inventory_data.get(pair);
            inventory_data.put(pair, (inventory_sold + 1L));
        }

        return inventory_data;
    }

    /**
     * Checks if restock of given item has occured after the timestamp
     * 
     * @param item_name item to check
     * @param timestamp timestamp to check
     * @return true if a restock has occured, false otherwise
     */
    public boolean restock(String item_name, Timestamp timestamp) {
        try {
            PreparedStatement statement = connection
                    .prepareStatement(
                            "SELECT shipment_date,item_name FROM shipment_history WHERE shipment_date > ? AND item_name = ?");
            statement.setTimestamp(1, timestamp);
            statement.setString(2, item_name);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            // here we opt to not propagate the exception, and instead just return false.
            System.err.println(e);
            return false;
        }
        return false;
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
                // if the username is valid, allow the login
                // TODO: check password
                return true;
            }
        } catch (SQLException e) {
            // here we opt to not propagate the exception, and instead just return false.
            // This is to ensure that the login does not erroneously pass
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
     * 
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
     * Gets the menu items from the database
     * 
     * @param menuNameString   name of the menu item
     * @param mealTypeField    meal type of the menu item
     * @param descriptionField description of the menu item
     * @param price_small      price of the small menu item
     * @param price_med        price of the medium menu item
     * @param price_large      price of the large menu item
     * 
     * @throws SQLException if the query fails
     */
    public void addMenuItem(String menuNameString, String mealTypeField, String descriptionField, Float price_small,
            Float price_med, Float price_large) throws SQLException {
        String query = "INSERT INTO menu_items (name, meal_type, description, price_small, price_med, price_large) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, menuNameString);
        stmt.setString(2, mealTypeField);
        stmt.setString(3, descriptionField);
        stmt.setFloat(4, price_small);
        stmt.setFloat(5, price_med);
        stmt.setFloat(6, price_large);
        stmt.executeUpdate();
    }

    /**
     * Adds the inventory item to the database
     * 
     * @param inventoryID      id for inventory item
     * @param itemName         name of inventory item
     * @param quantity         quantity of inventory it
     * @param shipmentSize     size of shipment of inventory item
     * @param restockThreshold threshold for inventory item to be restocked
     * 
     * @throws SQLException if the query fails
     */
    public void addInventoryItem(Long inventoryID, String itemName, Integer quantity, Integer shipmentSize,
            Integer restockThreshold)
            throws SQLException {
        String query = "INSERT INTO inventory_items (inventory_id, item_name, quantity, shipment_size, restock_threshold) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setLong(1, inventoryID);
        stmt.setString(2, itemName);
        stmt.setInt(3, quantity);
        stmt.setInt(4, shipmentSize);
        stmt.setInt(5, restockThreshold);
        stmt.executeUpdate();
    }

    /**
     * Adds the association to the database
     * 
     * @param menuID      id for menu item
     * @param inventoryID id for inventory item
     * 
     * @throws SQLException if the query fails
     */
    public void addAssociation(Long menuID, Long inventoryID)
            throws SQLException {
        String query = "INSERT INTO menu_inventory_assoc (menu_item_id, inventory_item_id) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setLong(1, menuID);
        stmt.setLong(2, inventoryID);
        stmt.executeUpdate();
    }

    /**
     * Gets the inventory items from the database
     * 
     * @param menu menu item to update
     * 
     * @throws SQLException if the query fails
     */
    public void updateMenu(MenuItem menu) throws SQLException {
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
    }

    /**
     * Gets the inventory items from the database
     * 
     * @param inventory inventory item to update
     * 
     * @throws SQLException if the query fails
     */
    public void updateInventory(InventoryItem inventory) throws SQLException {
        // Create an SQL statement to update the data
        String query = "UPDATE inventory_items SET item_name=?, quantity=?, shipment_size=?, restock_threshold=? WHERE inventory_id = ?";

        // Prepare the statement and set the parameters
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, inventory.itemName);
        stmt.setInt(2, inventory.quantity);
        stmt.setInt(3, inventory.shipmentSize);
        stmt.setInt(4, inventory.restockThreshold);
        stmt.setLong(5, inventory.inventoryId);

        // Execute the statement and check the number of rows affected
        int rows = stmt.executeUpdate();
        if (rows == 1) {
            System.out.println("Inventory item updated successfully");
        } else {
            System.out.println("Failed to update inventory item");
        }
    }

    /**
     * Updates the associations from the database
     * 
     * @param association association between menuId and inventoryId to update
     * 
     * @throws SQLException if the query fails
     */
    public void updateAssociations(Association association) throws SQLException {
        // Create an SQL statement to update the data
        String query = "UPDATE menu_inventory_assoc SET menu_item_id = ?, inventory_item_id = ? WHERE id = ?";

        // Prepare the statement and set the parameters
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setLong(2, association.inventoryId);
        stmt.setLong(1, association.menuId);
        stmt.setLong(3, association.id);
        // Execute the statement and check the number of rows affected
        int rows = stmt.executeUpdate();
        if (rows == 1) {
            System.out.println("Inventory item updated successfully");
        } else {
            System.out.println("Failed to update inventory item");
        }
    }

    /**
     * Gets the menu items from the database
     * 
     * @return the menu items from the database
     * 
     * @throws SQLException if the query fails
     */
    public ArrayList<MenuItem> getMenuItems() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM menu_items ORDER BY id");
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
     * Gets a correspondence between order ids and menu item ids with the given
     * timespan
     * 
     * @param start the start of the timespan
     * @param end   the end of the timespan
     * @return a correspondence between order ids and menu item ids
     * @throws SQLException if the query fails
     */
    public HashMap<Long, ArrayList<Long>> getOrdersToMenuItems(Timestamp start, Timestamp end) throws SQLException {
        String query = "SELECT a.order_id,a.menu_item_id FROM order_history o JOIN order_menu_assoc a ON a.order_id = o.id WHERE o.order_date BETWEEN ? AND ?";

        // Prepare the statement and set the parameters
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setTimestamp(1, start);
        stmt.setTimestamp(2, end);
        // Execute the statement
        ResultSet rs = stmt.executeQuery();
        HashMap<Long, ArrayList<Long>> association = new HashMap<>();
        while (rs.next()) {
            long order_id = rs.getLong("order_id");
            long menu_item_id = rs.getLong("menu_item_id");
            if (association.containsKey(order_id)) {
                association.get(order_id).add(menu_item_id);
            } else {
                ArrayList<Long> menuItems = new ArrayList<>();
                menuItems.add(menu_item_id);
                association.put(order_id, menuItems);
            }
        }
        return association;
    }

    /**
     * Gets the inventory items from the database
     * 
     * @return the inventory items from the database
     * 
     * @throws SQLException if the query fails
     */
    public ResultSet getInventoryItems() throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery("SELECT * FROM inventory_items ORDER BY inventory_id");
    }

    /**
     * Gets the inventory item names for all orders within a time window.
     * 
     * @param start Start of the time window.
     * @param end   End of the time window.
     * @return A list of inventory item names.
     * 
     * @throws SQLException if the query fails.
     */
    public ArrayList<String> getSalesData(Timestamp start, Timestamp end) throws SQLException {
        // set up query
        String query = "SELECT inventory_items.item_name "
                + "FROM order_history "
                + "JOIN order_menu_assoc "
                + "ON order_history.id = order_menu_assoc.order_id "
                + "JOIN menu_items "
                + "ON order_menu_assoc.menu_item_id = menu_items.id "
                + "JOIN menu_inventory_assoc "
                + "ON menu_items.id = menu_inventory_assoc.menu_item_id "
                + "JOIN inventory_items "
                + "ON menu_inventory_assoc.inventory_item_id = inventory_items.inventory_id "
                + "WHERE order_date BETWEEN ? AND ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setTimestamp(1, start);
        stmt.setTimestamp(2, end);

        // get inventory item names
        ArrayList<String> inventoryNames = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String name = rs.getString("item_name");
            inventoryNames.add(name);
        }

        return inventoryNames;
    }

    /**
     * Gets the last timestamp for an Z report.
     * 
     * @return The last timestamp.
     * 
     * @throws SQLException if the query fails.
     */
    public Timestamp getLastZReport() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT report_date FROM z_report_dates ORDER BY report_date DESC LIMIT 1");

        while (rs.next()) {
            return rs.getTimestamp("report_date");
        }

        throw new SQLException("No Z report found.");
    }

    /**
     * Adds the date of the last Z report to the SQL table.
     * 
     * @param reportDate
     * @throws SQLException
     */
    public void addZReport(Timestamp reportDate) throws SQLException {
        String query = "INSERT INTO z_report_dates (report_date) VALUES (?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setTimestamp(1, reportDate);
        stmt.executeUpdate();
    }

    /**
     * Gets the Associations from the database
     * 
     * @return the associations from the database
     * 
     * @throws SQLException if the query fails
     */
    public ResultSet getAssociations() throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery("SELECT * FROM menu_inventory_assoc ORDER BY menu_item_id");
    }

    /**
     * Gets the inventory items from the database
     * 
     * @return the inventory items from the database
     * 
     * @throws SQLException if the query fails
     */
    public ResultSet getRestockItems() throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt
                .executeQuery("SELECT * FROM inventory_items WHERE quantity < restock_threshold ORDER BY inventory_id");
    }
}
