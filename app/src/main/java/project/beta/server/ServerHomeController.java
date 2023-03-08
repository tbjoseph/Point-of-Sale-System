package project.beta.server;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import project.beta.server.OrderView;
import project.beta.server.OrderItem.OrderItemType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

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

    /**
     * Add a side item to the current order.
     * 
     * @param event Used to get the button name of the side item.
     */
    public void addSide(ActionEvent event) {
        Button sideButton = (Button) event.getSource();
        String sideName = sideButton.getText();

        currItem.menuItems.add(sideName);
        sidesCount++;
        
        if (currItem.type == OrderItem.OrderItemType.A_LA_CARTE ||
            (sidesCount == sidesCountNeeded && entreesCount == entreesCountNeeded)) {
            // update order view
            view.addOrderItem(currItem);
            view.updateView(viewBox);
            currItem = new OrderItem(
                new String[0], 1, OrderItem.OrderItemType.A_LA_CARTE
            );

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

        currItem.menuItems.add(entreeName);
        entreesCount++;

        if (currItem.type == OrderItem.OrderItemType.A_LA_CARTE ||
            (sidesCount == sidesCountNeeded && entreesCount == entreesCountNeeded)) {
            // update order view
            view.addOrderItem(currItem);
            view.updateView(viewBox);
            currItem = new OrderItem(
                new String[0], 1, OrderItem.OrderItemType.A_LA_CARTE
            );

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
        }
        else if (currItem.type == OrderItem.OrderItemType.BOWL) {
            currItem.type = OrderItem.OrderItemType.A_LA_CARTE;
            entreesCountNeeded = 1;

            bowlButton.setStyle("-fx-background-color: cherry;");
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
        }
        else if (currItem.type == OrderItem.OrderItemType.PLATE) {
            currItem.type = OrderItem.OrderItemType.A_LA_CARTE;
            entreesCountNeeded = 1;

            plateButton.setStyle("-fx-background-color: cherry;");
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
        }
        else if (currItem.type == OrderItem.OrderItemType.BIGGER_PLATE) {
            currItem.type = OrderItem.OrderItemType.A_LA_CARTE;
            entreesCountNeeded = 1;

            biggerPlateButton.setStyle("-fx-background-color: cherry;");
        }
    }

    /**
     * Toggle two sides for the current meal.
     */
    public void twoSides() {
        if (currItem.type == OrderItem.OrderItemType.A_LA_CARTE || (sidesCountNeeded == 2)) {
            twoSides.setStyle("-fx-background-color: cherry;");
            sidesCountNeeded = 1;
        }
        else {
            twoSides.setStyle("-fx-background-color: gray;");
            sidesCountNeeded = 2;
        }
    }

    public void nextScreen(ActionEvent event) throws IOException {
        // change the scene to the drinks and appetizer screen
        // TODO: pass the current order to the next screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("server_addons.fxml"));
        Parent root = loader.load();
        ServerController serverController = loader.getController();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("common.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("server_addons.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Set variables to default values.
     */
    public void initialize() {
        currItem = new OrderItem(
            new String[0], 1, OrderItem.OrderItemType.A_LA_CARTE
        );
        view = new OrderView();
        sidesCount = 0;
        sidesCountNeeded = 1;
        entreesCount = 0;
        entreesCountNeeded = 1;
    }
}
