package project.beta.reports;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;

// FIXME:
import java.io.*;

import project.beta.BackendDAO;

public class SalesReportController {
    private BackendDAO dao;
    private Timestamp startDate;
    private Timestamp endDate;
    private HashMap<String, Integer> inventorySold;

    @FXML
    private TableView<HashMap.Entry<String, Integer>> salesReportTable;

    @FXML
    private TableColumn<HashMap.Entry<String, Integer>, String> itemColumn;

    @FXML
    private TableColumn<HashMap.Entry<String, Integer>, Integer> countColumn;

    public void initialize() {

    }

    /**
     * Gets the data for the sales report and displays it.
     */
    public void SetupReport() {
        try {
            // get data
            ArrayList<String> inventoryNames = dao.getSalesData(startDate, endDate);

            // get a count of each unique inventory item
            inventorySold = new HashMap<>();
            for (String name : inventoryNames) {
                if (inventorySold.containsKey(name)) {
                    inventorySold.put(name, inventorySold.get(name) + 1);
                } else {
                    inventorySold.put(name, 1);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Displays the sales report in a table.
     * 
     * @param uniqueNames A list of inventory item names and their count.
     */
    public void displayReport() {
        ObservableList<HashMap.Entry<String, Integer>> list = FXCollections.observableArrayList();
        for (HashMap.Entry<String, Integer> entry : inventorySold.entrySet()) {
            list.add(entry);
        }

        itemColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
        countColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getValue()));

        salesReportTable.setItems(list);
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
     * Gets a time window for the sales report.
     * 
     * @param start Start time.
     * @param end   End time.
     */
    public void setInputs(Timestamp start, Timestamp end) {
        this.startDate = start;
        this.endDate = end;
    }
}
