package project.beta.server;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import project.beta.BackendDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Controller class for the server home screen. Generates an order and passes it
 * to the next screen.
 * 
 * @author Matthew Gimlin
 */
public class ServerHomeController {
    @FXML
    private VBox viewBox;
    @FXML
    private Button bowlButton;
    @FXML
    private Button plateButton;
    @FXML
    private Button biggerPlateButton;
    @FXML
    private Button twoSides;

    private OrderItem currItem;
    private OrderView view;

    private int sidesCount;
    private int sidesCountNeeded;
    private int entreesCount;
    private int entreesCountNeeded;

    private BackendDAO dao;

    /**
     * A default constructor for the controller. No initialization is done here.
     */
    public ServerHomeController() {
    }

    /**
     * Add a side item to the current order.
     * 
     * @param event Used to get the button name of the side item.
     */
    public void addSide(ActionEvent event) {
        Button sideButton = (Button) event.getSource();
        String sideName = sideButton.getText();

        if (sidesCount < sidesCountNeeded) {
            currItem.menuItems.add(sideName);
            sidesCount++;
        }

        if (currItem.type == OrderItem.OrderItemType.A_LA_CARTE ||
                (sidesCount == sidesCountNeeded && entreesCount == entreesCountNeeded)) {
            // update order view
            view.addOrderItem(currItem);
            view.updateView(viewBox);
            currItem = new OrderItem(
                    new String[0], 1, OrderItem.OrderItemType.A_LA_CARTE);

            // set to default values
            sidesCount = 0;
            sidesCountNeeded = 1;
            entreesCount = 0;
            entreesCountNeeded = 1;

            // set to default styles
            bowlButton.setStyle("-fx-background-color: cherry;");
            plateButton.setStyle("-fx-background-color: cherry;");
            biggerPlateButton.setStyle("-fx-background-color: cherry;");
            twoSides.setStyle("-fx-background-color: cherry;");
        }
    }

    /**
     * Add an entree to the current order.
     * 
     * @param event Used to get the button name of the entree.
     */
    public void addEntree(ActionEvent event) {
        Button entreeButton = (Button) event.getSource();
        String entreeName = entreeButton.getText();

        if (entreesCount < entreesCountNeeded) {
            currItem.menuItems.add(entreeName);
            entreesCount++;
        }

        if (currItem.type == OrderItem.OrderItemType.A_LA_CARTE ||
                (sidesCount == sidesCountNeeded && entreesCount == entreesCountNeeded)) {
            // update order view
            view.addOrderItem(currItem);
            view.updateView(viewBox);
            currItem = new OrderItem(
                    new String[0], 1, OrderItem.OrderItemType.A_LA_CARTE);

            // set to default values
            sidesCount = 0;
            sidesCountNeeded = 1;
            entreesCount = 0;
            entreesCountNeeded = 1;

            // set to default styles
            bowlButton.setStyle("-fx-background-color: cherry;");
            plateButton.setStyle("-fx-background-color: cherry;");
            biggerPlateButton.setStyle("-fx-background-color: cherry;");
            twoSides.setStyle("-fx-background-color: cherry;");
        }
    }

    /**
     * Group the next selected menu items into a bowl meal.
     */
    public void bowlMode() {
        if (currItem.type == OrderItem.OrderItemType.A_LA_CARTE) {
            currItem.type = OrderItem.OrderItemType.BOWL;
            entreesCountNeeded = 1;
            bowlButton.setStyle("-fx-background-color: gray;");
        } else if (currItem.type == OrderItem.OrderItemType.BOWL) {
            bowlButton.setStyle("-fx-background-color: cherry;");
            twoSides.setStyle("-fx-background-color: cherry;");

            currItem = new OrderItem(
                    new String[0], 1, OrderItem.OrderItemType.A_LA_CARTE);

            // set to default values
            sidesCount = 0;
            sidesCountNeeded = 1;
            entreesCount = 0;
            entreesCountNeeded = 1;
        }
    }

    /**
     * Group the next selected menu items into a plate meal.
     */
    public void plateMode() {
        if (currItem.type == OrderItem.OrderItemType.A_LA_CARTE) {
            currItem.type = OrderItem.OrderItemType.PLATE;
            entreesCountNeeded = 2;
            plateButton.setStyle("-fx-background-color: gray;");
        } else if (currItem.type == OrderItem.OrderItemType.PLATE) {
            plateButton.setStyle("-fx-background-color: cherry;");
            twoSides.setStyle("-fx-background-color: cherry;");

            currItem = new OrderItem(
                    new String[0], 1, OrderItem.OrderItemType.A_LA_CARTE);

            // set to default values
            sidesCount = 0;
            sidesCountNeeded = 1;
            entreesCount = 0;
            entreesCountNeeded = 1;
        }
    }

    /**
     * Group the next selected menu items into a bigger plate meal.
     */
    public void biggerPlateMode() {
        if (currItem.type == OrderItem.OrderItemType.A_LA_CARTE) {
            currItem.type = OrderItem.OrderItemType.BIGGER_PLATE;
            entreesCountNeeded = 3;
            biggerPlateButton.setStyle("-fx-background-color: gray;");
        } else if (currItem.type == OrderItem.OrderItemType.BIGGER_PLATE) {
            biggerPlateButton.setStyle("-fx-background-color: cherry;");
            twoSides.setStyle("-fx-background-color: cherry;");

            currItem = new OrderItem(
                    new String[0], 1, OrderItem.OrderItemType.A_LA_CARTE);

            // set to default values
            sidesCount = 0;
            sidesCountNeeded = 1;
            entreesCount = 0;
            entreesCountNeeded = 1;
        }
    }

    /**
     * Toggle two sides for the current meal.
     */
    public void twoSides() {
        if (currItem.type == OrderItem.OrderItemType.A_LA_CARTE || (sidesCountNeeded == 2)) {
            twoSides.setStyle("-fx-background-color: cherry;");
            sidesCountNeeded = 1;
        } else {
            twoSides.setStyle("-fx-background-color: gray;");
            sidesCountNeeded = 2;
        }
    }

    /**
     * Changes the scene to the next page, server addons.
     * 
     * @param event Used to get the current stage.
     * @throws IOException if the file cannot be loaded.
     */
    public void nextScreen(ActionEvent event) throws IOException {
        // change the scene to the drinks and appetizer screen
        // TODO: pass the current order to the next screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../server_addons.fxml"));
        Parent root = loader.load();
        ServerAddonsController serverController = loader.getController();
        serverController.setDAO(dao);
        serverController.setOrders(view);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("../server_addons.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Set variables to default values.
     */
    public void initialize() {
        currItem = new OrderItem(
                new String[0], 1, OrderItem.OrderItemType.A_LA_CARTE);
        sidesCount = 0;
        sidesCountNeeded = 1;
        entreesCount = 0;
        entreesCountNeeded = 1;
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
     * Pass down the order view to use for this controller
     * 
     * @param view the order view to use for this controller
     */
    public void setOrders(OrderView view) {
        this.view = view;
        view.updateView(viewBox);
    }
}
