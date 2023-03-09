package project.beta.server;

import project.beta.BackendDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;

import java.time.LocalDateTime;

import project.beta.server.ServerController;
import project.beta.types.MenuItem;
import project.beta.types.OrderItem;
import project.beta.types.OrderView;

/**
 * Controller class for the server home screen. Generates an order and passes it
 * to the next screen.
 * 
 * @author Timothy Joseph
 */
public class ServerController {
    private BackendDAO dao;
    private OrderView orders;

    @FXML
    private VBox viewBox;
    @FXML
    private VBox errorPane;
    @FXML
    private Label errorText;

    /**
     * Constructor for ServerController. Initialization is done in initialize()
     */
    public ServerController() {

    }

    /**
     * Initialize the controller
     */
    public void initialize() {
        errorPane.setVisible(false);
    }

    /**
     * Back button to get to addons
     * 
     * @param event the event that triggered the function
     * @throws IOException if the file is not found
     */
    public void backButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../server_addons.fxml"));
        Parent root = loader.load();

        ServerAddonsController serverController = loader.getController();
        serverController.setDAO(dao);
        serverController.setOrders(orders);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Post checkout function to get to home
     * 
     * @param event the event that triggered the function
     */
    public void goToHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../server_home.fxml"));
            Parent root = loader.load();

            ServerHomeController serverController = loader.getController();
            serverController.setDAO(dao);
            serverController.setOrders(new OrderView());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            handleError(e);
        }
    }

    /**
     * Return total price
     * 
     * @return priceSum the total price
     */
    public float getPrice() {
        float priceSum = 0;

        for (OrderItem item : orders.getOrders()) {
            switch (item.type) {
                case BOWL:
                    priceSum += 6.80;
                    break;

                case PLATE:
                    priceSum += 7.60;
                    break;

                case BIGGER_PLATE:
                    priceSum += 8.30;
                    break;

                case A_LA_CARTE:
                    for (MenuItem menuItem : item.menuItems) {
                        priceSum += menuItem.priceSmall;
                    }
                    break;

                default:
                    break;
            }
        }

        return priceSum;
    }

    /**
     * Update data base for cash payment
     * 
     * @param event the event that triggered the function
     */
    public void processCash(ActionEvent event) {
        try {
            // Add order to order history
            dao.submitOrder("cash", LocalDateTime.now(), getPrice(), orders);
            // Decrease inventory items
            // dao.executeQuery("f");

            goToHome(event);

        } catch (SQLException e) {
            handleError(e);
        }
    }

    /**
     * Update data base for card payment
     * 
     * @param event the event that triggered the function
     */
    public void processCard(ActionEvent event) {
        try {
            dao.submitOrder("card", LocalDateTime.now(), getPrice(), orders);

            // Decrease inventory items
            // dao.executeQuery("f");

            goToHome(event);

        } catch (SQLException e) {
            handleError(e);
        }
    }

    /**
     * Update data base for dining payment
     * 
     * @param event the event that triggered the function
     */
    public void processDining(ActionEvent event) {
        try {
            dao.submitOrder("dining_dollars", LocalDateTime.now(), getPrice(), orders);

            // Decrease inventory items
            // dao.executeQuery("f");

            goToHome(event);

        } catch (SQLException e) {
            handleError(e);
        }
    }

    /**
     * Update data base for meal plan payment
     * 
     * @param event the event that triggered the function
     */
    public void processMealPlan(ActionEvent event) {
        try {
            dao.submitOrder("meal_plan_both", LocalDateTime.now(), getPrice(), orders);
            //TODO: add functionality to update order_menu_assoc table.
            // - Make hash table that maps each (temp) order_id to all of their respective menu_item ids. 
            //   The actual order_id will be decided in the SQL query.
            // - Examine order_ids (outer array) and their respective menu_items (inner array) until hash is empty:
            //      - For a given menu_item, count number of instances, and save count as quantity
            //      - Add the order-menu_item pair to assoc table via SQL query. Format is (order_id, menu_item_id, quantity)
            //      - Delete all instance of that menu_item in current inner array to avoid double counting
            // - Hash should now be empty, ready for next order list


            
            /**
             * TODO: add functionality to decrease inventory items using menu_inventory_assoc table
             * - Make hash table that maps each menu_id to all of their respective inventory_item ids
             * - Initialize list of all Inventory Ids
             * - Iterate through OrderView list
             *      - Iterate through each OrderItem in the OrderItem list
             *          - add each inventory id that maps to the order item id (index) in the hash to the Inventory Id list
             * - SQL query to decrease quantity of respective inventory_id each time the inventory_id appear in Inventory Id.
             *   Should be something like this:
             *         - UPDATE inventory_items SET quantity = quantity - 1
             *           WHERE inventory_id IN ( {ArrayList of inventory ids} )
             */


            // dao.decreaseInventory;


            goToHome(event);

        } catch (SQLException e) {
            handleError(e);
        }
    }

    /**
     * Pass down the DAO to use for this controller
     * 
     * @param dao the DAO to use for this controller
     */
    public void setDAO(BackendDAO dao) {
        this.dao = dao;
    }

    /**
     * Pass down the orders to use for this controller
     * 
     * @param orders the orders to use for this controller
     */
    public void setOrders(OrderView orders) {
        this.orders = orders;
        orders.updateView(viewBox);
    }

    /**
     * Handles a SQLException by printing the error to the console and setting the
     * errorText label.
     * 
     * @param exception - the exception to handle
     */
    private void handleError(SQLException exception) {
        errorPane.setVisible(true);
        errorText.textProperty().set("Warning: an error occurred with the database. See the log for details.");
        exception.printStackTrace();
    }

    /**
     * Handles an IOException by printing the error to the console and setting the
     * errorText label.
     * 
     * @param exception - the exception to handle
     */
    private void handleError(IOException exception) {
        errorPane.setVisible(true);
        errorText.textProperty().set("Warning: an error when changing scenes. See the log for details.");
        exception.printStackTrace();
    }

    /**
     * Closes the error pane.
     * 
     * @param event - the event that triggered the function
     */
    public void closeErrorPane(ActionEvent event) {
        errorPane.setVisible(false);
    }
}