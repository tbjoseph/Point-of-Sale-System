package project.beta.server;

import javafx.event.ActionEvent;
import project.beta.BackendDAO;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

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
     * Pass down the DAO to use for this controller
     * 
     * @param dao the DAO to use for this controller
     */
    public void setDAO(BackendDAO dao) {
        this.dao = dao;
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
