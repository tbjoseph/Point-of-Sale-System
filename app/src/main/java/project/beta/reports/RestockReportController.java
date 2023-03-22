package project.beta.reports;

import project.beta.BackendDAO;
import project.beta.types.InventoryItem;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for the restock report.
 * 
 * @author Joshua Downey
 */
public class RestockReportController {
    private BackendDAO dao;
    @FXML
    private Label errorText;

    @FXML
    private TableView<InventoryItem> inventoryTable;
    @FXML
    private TableColumn<InventoryItem, Long> inventoryIdTableCol;
    @FXML
    private TableColumn<InventoryItem, String> itemNameCol;
    @FXML
    private TableColumn<InventoryItem, Integer> quantityCol;
    @FXML
    private TableColumn<InventoryItem, Integer> shipmentSizeCol;
    @FXML
    private TableColumn<InventoryItem, Integer> thresholdCol;

    /**
     * Constructor for the restock report controller. setUp does the actual work.
     */
    public RestockReportController() {
    }

    /**
     * Sets up the table view with the data from the database.
     * This should be called after the DAO is set.
     */
    public void setUp() {
        try {
            ResultSet rs = dao.getRestockItems();

            inventoryIdTableCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Long>(r.getValue().inventoryId));
            itemNameCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<String>(r.getValue().itemName));
            quantityCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Integer>(r.getValue().quantity));
            shipmentSizeCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Integer>(r.getValue().shipmentSize));
            thresholdCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Integer>(r.getValue().restockThreshold));
            while (rs.next()) {
                Long inventoryId = rs.getLong("inventory_id");
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                int shipmentSize = rs.getInt("shipment_size");
                int restockThreshold = rs.getInt("restock_threshold");
                InventoryItem item = new InventoryItem(inventoryId, itemName, quantity, shipmentSize, restockThreshold);
                inventoryTable.getItems().add(item);
            }
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
        setUp();
    }

    /**
     * Handles a SQLException by printing the error to the console and setting the
     * errorText label.
     * 
     * @param exception - the exception to handle
     */
    private void handleError(SQLException exception) {
        errorText.textProperty().set("Warning: an error occurred with the database. See the log for details.");
        exception.printStackTrace();
    }

    /**
     * Goes back to the report home page
     * 
     * @param event the event that triggered the function
     * @throws IOException if the fxml file cannot be loaded
     */
    public void goBack(ActionEvent event) throws IOException {
        // get the report home controller and load it
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent root = loader.load();
        ReportsHomeController controller = loader.getController();
        controller.setDAO(dao);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }
}
