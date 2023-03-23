package project.beta.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.SQLException;
import javafx.util.Pair;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.ReadOnlyObjectWrapper;


import project.beta.BackendDAO;

/**
 * ExcessReportController is a class that controls the excess report screen.
 * 
 * @author Timothy Joseph
 */
public class ExcessReportController {
    private BackendDAO dao;
    private Timestamp timestamp;
    private HashMap<Pair<String, Long>, Long> inventory_data;
    private ArrayList<String> itemNameList;
    private ArrayList<Long> currentInventoryList;
    private ArrayList<Long> previousInventoryList;

    @FXML
    private TableView<InventoryItem> inventoryTable;
    @FXML
    private TableColumn<InventoryItem, String> itemNameCol;
    @FXML
    private TableColumn<InventoryItem, Long> currentInventoryCol;
    @FXML
    private TableColumn<InventoryItem, Long> previousInventoryCol;


    /**
     * Constructor for the ExcessReportController
     */
    public ExcessReportController() {
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
     * Sets the input for the report
     * 
     * @param timestamp the timestamp to use for the report
     */
    public void setInput(Timestamp timestamp) {
        this.timestamp = timestamp;

        try {
            inventory_data = dao.construct_inventory_data(this.timestamp);
            output_excess();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs the ArrayLists for table values
     */
    public void output_excess() {
        itemNameList = new ArrayList<>();
        currentInventoryList = new ArrayList<>();
        previousInventoryList = new ArrayList<>();

        for (Pair<String, Long> item : inventory_data.keySet()) {

            if (dao.restock(item.getKey(), timestamp))
                continue;

            Long current_inventory = item.getValue();
            Long inventory_sold = inventory_data.get(item);

            double excess_ratio = (double) inventory_sold / (double) (current_inventory + inventory_sold);

            if (excess_ratio > 0.1)
                continue;

            itemNameList.add(item.getKey());
            currentInventoryList.add(current_inventory);
            previousInventoryList.add(current_inventory + inventory_sold);

        }
        construct_table();
    }

    /**
     * Sets up the table view with the data from the ArrayLists
     */
    public void construct_table() {
        itemNameCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<String>(r.getValue().itemName));
        currentInventoryCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Long>(r.getValue().currentInventory));
        previousInventoryCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<Long>(r.getValue().previousInventory));

        for (int i = 0; i < itemNameList.size(); i++) {
            String itemName = itemNameList.get(i);
            Long currentInventory = currentInventoryList.get(i);
            Long previousInventory = previousInventoryList.get(i);
            InventoryItem item = new InventoryItem(itemName, currentInventory, previousInventory);
            inventoryTable.getItems().add(item);
        }
    }


    /**
     * Class to represent items in the table
     */
    public static class InventoryItem {
        private String itemName;
        private Long currentInventory;
        private Long previousInventory;

        public InventoryItem(String itemName, Long currentInventory, Long previousInventory) {
            this.itemName = itemName;
            this.currentInventory = currentInventory;
            this.previousInventory = previousInventory;
        }
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