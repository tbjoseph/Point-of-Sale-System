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
import java.time.format.DateTimeFormatter;



import project.beta.server.ServerController;


public class ServerController {
    private BackendDAO dao;
    private OrderView orders;

    @FXML
    private VBox orderView;

    /**
     * Constructor for ServerController
     * @author Timothy Joseph
     */
    public ServerController() {

    }

    /**
     * Back button to get to addons
     * @author Timothy Joseph
     * @param event
     * @throws IOException
     */
    public void backButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../server_addons.fxml"));
        Parent root = loader.load();

        ServerController serverController = loader.getController();
        serverController.setDAO(dao);


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("../server_addons.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Post checkout function to get to home
     * @author Timothy Joseph
     * @param event
     * @throws IOException
     */
    public void goToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../server_home.fxml"));
        Parent root = loader.load();

        ServerController serverController = loader.getController();
        serverController.setDAO(dao);


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("../server_home.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Return current date
     * @author Timothy Joseph
     * @return
     */
    public String getDate() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Format the date and time using a DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        // Return the formatted date and time
        return formattedDateTime;
    }

    /**
     * Return total price
     * @author Timothy Joseph
     * @return
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
     * @author Timothy Joseph
     * @param event
     */
    public void processCash(ActionEvent event) {
        try {
            String paymentMethod = "cash";
            String date = getDate();
            float price = getPrice();
            
            // Add order to order history
            String queryBeginning = "INSERT INTO order_history (order_date, price, payment_method) ";
            String queryEnd = "VALUES ('" + date + "', " + price + ", '" + paymentMethod + "')";
            
            dao.executeQuery(queryBeginning + queryEnd);

            // Decrease inventory items
            // dao.executeQuery("f");

            goToHome(event);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update data base for card payment
     * @author Timothy Joseph
     * @param event
     */
    public void processCard(ActionEvent event) {
        try {
            String paymentMethod = "card";
            String date = getDate();
            float price = getPrice();
            
            // Add order to order history
            String queryBeginning = "INSERT INTO order_history (order_date, price, payment_method) ";
            String queryEnd = "VALUES ('" + date + "', " + price + ", '" + paymentMethod + "')";
            
            dao.executeQuery(queryBeginning + queryEnd);

            // Decrease inventory items
            // dao.executeQuery("f");

            goToHome(event);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update data base for dining payment
     * @author Timothy Joseph
     * @param event
     */
    public void processDining(ActionEvent event) {
        try {
            String paymentMethod = "dining_dollars";
            String date = getDate();
            float price = getPrice();
            
            // Add order to order history
            String queryBeginning = "INSERT INTO order_history (order_date, price, payment_method) ";
            String queryEnd = "VALUES ('" + date + "', " + price + ", '" + paymentMethod + "')";
            
            dao.executeQuery(queryBeginning + queryEnd);

            // Decrease inventory items
            // dao.executeQuery("f");

            goToHome(event);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update data base for meal plan payment
     * @author Timothy Joseph
     * @param event
     */
    public void processMealPlan(ActionEvent event) {
        try {
            String paymentMethod = "meal_plan_both";
            String date = getDate();
            float price = getPrice();
            
            // Add order to order history
            String queryBeginning = "INSERT INTO order_history (order_date, price, payment_method) ";
            String queryEnd = "VALUES ('" + date + "', " + price + ", '" + paymentMethod + "')";
            
            dao.executeQuery(queryBeginning + queryEnd);

            // Decrease inventory items
            // dao.executeQuery("f");

            goToHome(event);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pass down the DAO to use for this controller
     * @author Timothy Joseph
     * @param dao - the DAO to use for this controller
     */
    public void setDAO(BackendDAO dao) {
        this.dao = dao;
    }
}
