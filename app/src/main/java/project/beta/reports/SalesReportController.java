package project.beta.reports;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import project.beta.BackendDAO;

/**
 * SalesReportController is a class that controls the sales report screen.
 * 
 * @author Matthew Gimlin
 */
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

    /**
     * Constructor for the SalesReportController
     */
    public SalesReportController() {
    }

    /**
     * Gets the data for the sales report and displays it.
     * 
     * @throws SQLException if the query fails.
     */
    public void setupSalesReport() throws SQLException {
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
    }

    /**
     * Gets the start and end date for the X report.
     * 
     * @throws SQLException if the query fails.
     */
    public void setupXReport() throws SQLException {
        startDate = dao.getLastZReport();
        endDate = new Timestamp(System.currentTimeMillis());

        this.setupSalesReport();
    }

    /**
     * Gets the start and end date for the Z report.
     * 
     * @throws SQLException if the query fails.
     */
    public void setupZReport() throws SQLException {
        startDate = dao.getLastZReport();
        endDate = new Timestamp(System.currentTimeMillis());
        dao.addZReport(endDate);

        this.setupSalesReport();
    }

    /**
     * Displays the sales report in a table.
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
        stage.show();
    }
}
