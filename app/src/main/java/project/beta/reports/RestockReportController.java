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
    private TableColumn<InventoryItem, Integer> restockThreshold;

    public void initialize() {

    }

    public void setUp() {
        try {
            ResultSet rs = dao.getRestockItems();

            inventoryIdTableCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Long>(r.getValue().inventoryId));
            itemNameCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<String>(r.getValue().itemName));
            quantityCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Integer>(r.getValue().quantity));
            shipmentSizeCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Integer>(r.getValue().shipmentSize));
            while (rs.next()) {
                Long inventoryId = rs.getLong("inventory_id");
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                int shipmentSize = rs.getInt("shipment_size");
                InventoryItem item = new InventoryItem(inventoryId, itemName, quantity, shipmentSize);
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
}
