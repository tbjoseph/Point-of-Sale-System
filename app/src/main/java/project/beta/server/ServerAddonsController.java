package project.beta.server;

import javafx.event.ActionEvent;
import project.beta.BackendDAO;
import project.beta.types.MenuItem;
import project.beta.types.OrderItem;
import project.beta.types.OrderView;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

/**
 * ServerAddonsController is a class that controls the addons screen.
 * 
 * @author Joshua Downey
 */
public class ServerAddonsController {
    private BackendDAO dao;
    @FXML
    VBox orderView;
    OrderView view;

    @FXML
    private HBox addonsCont;

    /**
     * A default constructor for ServerAddonsController
     */
    public ServerAddonsController() {
    }

    /**
     * adds an item to the order view
     * 
     * @param event the event that triggered the method
     */
    public void addItem(ActionEvent event) {
        Button bt = (Button) event.getSource();
        String name = bt.getText();
        String[] names = { name };
        OrderItem item = new OrderItem(names, 1, OrderItem.OrderItemType.A_LA_CARTE);
        view.addOrderItem(item);
        view.updateView(orderView);
    }

    /**
     * moves the page back to the entree/side screen
     * 
     * @param event the event that triggered the method
     * @throws IOException if the file is not found
     */
    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../server_home.fxml"));
        Parent root = loader.load();
        ServerHomeController serverController = loader.getController();
        serverController.setDAO(dao);
        serverController.setOrders(view);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("../server_home.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * moves the page over to the payment screen
     * 
     * @param event the event that triggered the method
     * @throws IOException if the file is not found
     */
    public void next(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../server_checkout.fxml"));
        Parent root = loader.load();
        ServerController serverController = loader.getController();
        serverController.setDAO(dao);
        serverController.setOrders(view);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("../server_checkout.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Dynamically creates buttons for each menu item based on the database.
     * 
     * @throws SQLException if a database error occurs.
     */
    public void createButtons() throws SQLException {
        addonsCont.getChildren().clear();
        // create buttons for each menu item
        for (MenuItem item : dao.getMenuItems()) {
            Button button = new Button(item.name);
            button.setOnAction(e -> {
                this.addItem(e);
            });
            HBox.setHgrow(button, Priority.ALWAYS);
            VBox.setVgrow(button, Priority.ALWAYS);
            button.maxHeight(Double.MAX_VALUE);
            button.maxWidth(Double.MAX_VALUE);

            switch (item.mealType) {
                case "appetizer":
                    addonsCont.getChildren().add(button);
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * Pass down the DAO to use for this controller
     * 
     * @param dao the DAO to use for this controller
     */
    public void setDAO(BackendDAO dao) {
        this.dao = dao;
        try {
            createButtons();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the order view from the previous screen
     * 
     * @param view the order view
     */
    public void setOrders(OrderView view) {
        this.view = view;
        view.updateView(orderView);
    }
}
