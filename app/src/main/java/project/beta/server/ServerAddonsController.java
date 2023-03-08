package project.beta.server;

import javafx.event.ActionEvent;
import project.beta.BackendDAO;
import javafx.scene.control.Button;
import project.beta.server.OrderView;
import project.beta.server.OrderItem;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class ServerAddonsController {
    private BackendDAO dao;
    @FXML
    VBox orderView;
    OrderView view;

    public ServerAddonsController() {
        view = new OrderView();
    }

    public void initialize() {
        view.updateView(orderView);
    }

    public void addItem(ActionEvent event) {
        Button bt = (Button) event.getSource();
        String name = bt.getText();
        String[] names = { name };
        OrderItem item = new OrderItem(names, 1, OrderItem.OrderItemType.A_LA_CARTE);
        view.addOrderItem(item);
        view.updateView(orderView);
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../server_home.fxml"));
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

    public void next(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../server_checkout.fxml"));
        Parent root = loader.load();
        ServerController serverController = loader.getController();
        serverController.setDAO(dao);
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
     * @param dao - the DAO to use for this controller
     */
    public void setDAO(BackendDAO dao) {
        this.dao = dao;
    }
}
