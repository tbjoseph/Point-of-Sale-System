package project.beta.server;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import project.beta.BackendDAO;
import project.beta.types.MenuItem;
import project.beta.types.OrderItem;
import project.beta.types.OrderView;
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

    @FXML
    private HBox sidesCont;
    @FXML
    private GridPane entreesCont;

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
     * @param item The item to add to the order.
     */
    public void addSide(MenuItem item) {
        if (sidesCount < sidesCountNeeded) {
            currItem.menuItems.add(item);
            sidesCount++;
        }

        if (currItem.type == OrderItem.OrderItemType.A_LA_CARTE ||
                (sidesCount == sidesCountNeeded && entreesCount == entreesCountNeeded)) {
            // update order view
            view.addOrderItem(currItem);
            view.updateView(viewBox);
            currItem = new OrderItem(
                    new MenuItem[0], 1, OrderItem.OrderItemType.A_LA_CARTE);

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
     * @param item The item to add to the order.
     */
    public void addEntree(MenuItem item) {
        if (entreesCount < entreesCountNeeded) {
            currItem.menuItems.add(item);
            entreesCount++;
        }

        if (currItem.type == OrderItem.OrderItemType.A_LA_CARTE ||
                (sidesCount == sidesCountNeeded && entreesCount == entreesCountNeeded)) {
            // update order view
            view.addOrderItem(currItem);
            view.updateView(viewBox);
            currItem = new OrderItem(
                    new MenuItem[0], 1, OrderItem.OrderItemType.A_LA_CARTE);

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
                    new MenuItem[0], 1, OrderItem.OrderItemType.A_LA_CARTE);

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
                    new MenuItem[0], 1, OrderItem.OrderItemType.A_LA_CARTE);

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
                    new MenuItem[0], 1, OrderItem.OrderItemType.A_LA_CARTE);

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addons.fxml"));
        Parent root = loader.load();
        ServerAddonsController serverController = loader.getController();
        serverController.setDAO(dao);
        serverController.setOrders(view);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Set variables to default values.
     */
    public void initialize() {
        currItem = new OrderItem(
                new MenuItem[0], 1, OrderItem.OrderItemType.A_LA_CARTE);
        sidesCount = 0;
        sidesCountNeeded = 1;
        entreesCount = 0;
        entreesCountNeeded = 1;
    }

    /**
     * Dynamically creates buttons for each menu item based on the database.
     * 
     * @throws SQLException if a database error occurs.
     */
    public void createButtons() throws SQLException {
        sidesCont.getChildren().clear();
        entreesCont.getChildren().clear();
        // create buttons for each menu item
        int column = 0;
        int row = 0;
        for (MenuItem item : dao.getMenuItems()) {
            Button button = new Button(item.name);
            button.setMaxHeight(Double.MAX_VALUE);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setWrapText(true);
            button.setTextAlignment(TextAlignment.CENTER);

            switch (item.mealType) {
                case "entree":
                case "premium entree":
                    button.setOnAction(e -> {
                        this.addEntree(item);
                    });
                    GridPane.setColumnIndex(button, column);
                    GridPane.setRowIndex(button, row);
                    GridPane.setHgrow(button, Priority.ALWAYS);
                    GridPane.setVgrow(button, Priority.ALWAYS);
                    column += 1;
                    if (column == 4) {
                        column = 0;
                        row += 1;
                    }
                    entreesCont.getChildren().add(button);
                    break;
                case "side":
                    button.setOnAction(e -> {
                        this.addSide(item);
                    });
                    HBox.setHgrow(button, Priority.ALWAYS);
                    sidesCont.getChildren().add(button);
                    break;

                default:
                    break;
            }
            button.autosize();
        }
        entreesCont.autosize();
        sidesCont.autosize();
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
     * Pass down the order view to use for this controller
     * 
     * @param view the order view to use for this controller
     */
    public void setOrders(OrderView view) {
        this.view = view;
        view.updateView(viewBox);
    }
}
