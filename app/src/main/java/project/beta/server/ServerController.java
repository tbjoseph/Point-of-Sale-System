package project.beta.server;

import project.beta.BackendDAO;

import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
// import javafx.scene.control.Label;
// import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXML;

import java.time.LocalDateTime;

import project.beta.server.ServerController;

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
    private VBox orderView;

    /**
     * Constructor for ServerController
     */
    public ServerController() {

    }

    /**
     * Back button to get to addons
     * 
     * @param event - the event that triggered the function
     * @throws IOException - if the file is not found
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
        scene.getStylesheets().add(getClass().getResource("../server_addons.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Post checkout function to get to home
     * 
     * @param event - the event that triggered the function
     * @throws IOException - if the file is not found
     */
    public void goToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../server_home.fxml"));
        Parent root = loader.load();

        ServerHomeController serverController = loader.getController();
        serverController.setDAO(dao);
        serverController.setOrders(new OrderView());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("../server_home.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Return total price
     * 
     * @return priceSum - the total price
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
                    priceSum += 4.10;
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
     * @param event - the event that triggered the function
     */
    public void processCash(ActionEvent event) {
        try {
            // Add order to order history
            dao.submitOrder("cash", LocalDateTime.now(), getPrice());
            // Decrease inventory items
            // dao.executeQuery("f");

            goToHome(event);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update data base for card payment
     * 
     * @param event - the event that triggered the function
     */
    public void processCard(ActionEvent event) {
        try {
            dao.submitOrder("card", LocalDateTime.now(), getPrice());

            // Decrease inventory items
            // dao.executeQuery("f");

            goToHome(event);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update data base for dining payment
     * 
     * @param event - the event that triggered the function
     */
    public void processDining(ActionEvent event) {
        try {
            dao.submitOrder("dining_dollars", LocalDateTime.now(), getPrice());

            // Decrease inventory items
            // dao.executeQuery("f");

            goToHome(event);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update data base for meal plan payment
     * 
     * @param event - the event that triggered the function
     */
    public void processMealPlan(ActionEvent event) {
        try {
            dao.submitOrder("meal_plan_both", LocalDateTime.now(), getPrice());

            // Decrease inventory items
            // dao.executeQuery("f");

            goToHome(event);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pass down the DAO to use for this controller
     * 
     * @param dao - the DAO to use for this controller
     */
    public void setDAO(BackendDAO dao) {
        this.dao = dao;
    }

    /**
     * Pass down the orders to use for this controller
     * 
     * @param orders - the orders to use for this controller
     */
    public void setOrders(OrderView orders) {
        this.orders = orders;
        orders.updateView(orderView);
    }
}
