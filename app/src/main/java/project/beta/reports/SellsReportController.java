package project.beta.reports;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import project.beta.BackendDAO;
import project.beta.types.MenuItem;

/**
 * Controller for What Sells Together
 * 
 * (Teams of 5+ Only) Given a time window, display a list of pairs of menu items
 * that sell together often, popular or not, sorted by most frequent.
 * 
 * @author Griffith Thomas
 */
public class SellsReportController {
    private BackendDAO dao;
    private Timestamp startDate;
    private Timestamp endDate;

    private class TableRow {
        public String menuNames;
        public float frequency;
        public float priceA;
        public float priceB;

        public TableRow(String menuNames, float frequency, float priceA, float priceB) {
            this.menuNames = menuNames;
            this.frequency = frequency;
            this.priceA = priceA;
            this.priceB = priceB;
        }
    }

    @FXML
    private TableView<TableRow> resultsTable;
    @FXML
    private TableColumn<TableRow, String> freqCol;
    @FXML
    private TableColumn<TableRow, String> priceColA;
    @FXML
    private TableColumn<TableRow, String> priceColB;
    @FXML
    private TableColumn<TableRow, String> menuNamesCol;

    @FXML
    private VBox errorPane;
    @FXML
    private Label errorText;

    /**
     * Constructor for SellsReportController. Initialization is done in initialize()
     */
    public SellsReportController() {
    }

    /**
     * Initializes the controller class. This method is automatically called after
     * the fxml file has been loaded.
     */
    public void initialize() {
        errorPane.setVisible(false);
        menuNamesCol.setCellValueFactory(r -> new ReadOnlyObjectWrapper<String>(r.getValue().menuNames));
        freqCol.setCellValueFactory(
                r -> new ReadOnlyObjectWrapper<String>(String.format("%.2f %%", r.getValue().frequency * 100)));
        priceColA.setCellValueFactory(r -> new ReadOnlyObjectWrapper<String>(
                String.format("$%.2f", r.getValue().priceA)));
        priceColB.setCellValueFactory(r -> new ReadOnlyObjectWrapper<String>(
                String.format("$%.2f", r.getValue().priceB)));
    }

    /**
     * Collects the data from the database and displays it in the table
     */
    private void generateReport() {
        // get association table between orders and menu items
        try {
            HashMap<Long, ArrayList<Long>> mapping = dao.getOrdersToMenuItems(startDate, endDate);
            // create the count of each pair of menu items
            HashMap<Pair<Long, Long>, Integer> counts = new HashMap<>();
            HashMap<Long, Integer> totalCounts = new HashMap<>();
            for (Long order : mapping.keySet()) {
                ArrayList<Long> menuItems = mapping.get(order);
                for (int i = 0; i < menuItems.size(); i++) {
                    for (int j = i + 1; j < menuItems.size(); j++) {
                        Pair<Long, Long> pair = new Pair<>(menuItems.get(i), menuItems.get(j));
                        if (counts.containsKey(pair)) {
                            counts.put(pair, counts.get(pair) + 1);
                        } else {
                            counts.put(pair, 1);
                        }
                        if (totalCounts.containsKey(menuItems.get(i))) {
                            totalCounts.put(menuItems.get(i), totalCounts.get(menuItems.get(i)) + 1);
                        } else {
                            totalCounts.put(menuItems.get(i), 1);
                        }
                        if (totalCounts.containsKey(menuItems.get(j))) {
                            totalCounts.put(menuItems.get(j), totalCounts.get(menuItems.get(j)) + 1);
                        } else {
                            totalCounts.put(menuItems.get(j), 1);
                        }
                    }
                }
            }

            ArrayList<MenuItem> items = dao.getMenuItems();
            HashMap<Long, MenuItem> itemTable = new HashMap<>();
            items.iterator().forEachRemaining(m -> itemTable.put(m.getIndex(), m));
            // put items in the table
            for (Pair<Long, Long> pair : counts.keySet()) {
                String menuNames = itemTable.get(pair.getKey()).name + ", " + itemTable.get(pair.getValue()).name;
                Float freq = (float) counts.get(pair)
                        / (totalCounts.get(pair.getKey()) + totalCounts.get(pair.getValue()));
                // we only care about the large price
                Float priceA = itemTable.get(pair.getKey()).priceLarge * totalCounts.get(pair.getKey());
                Float priceB = itemTable.get(pair.getValue()).priceLarge * totalCounts.get(pair.getValue());
                resultsTable.getItems().add(new TableRow(menuNames, freq, priceA, priceB));
            }
            resultsTable.getItems().sort((a, b) -> (int) Math.signum(b.frequency - a.frequency));
            // remove items that are less than 1%
            resultsTable.getItems().removeIf(p -> p.frequency < 0.01);
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
    }

    /**
     * Sets the start and end dates for the report
     * 
     * @param start the start timestamp
     * @param end   the end timestamp
     */
    public void setInputs(Timestamp start, Timestamp end) {
        this.startDate = start;
        this.endDate = end;
        generateReport();
    }

    /**
     * Handles a SQLException by printing the error to the console and setting the
     * errorText label.
     * 
     * @param exception the exception to handle
     */
    private void handleError(SQLException exception) {
        errorPane.setVisible(true);
        errorText.textProperty().set("Warning: " + exception.getMessage());
        exception.printStackTrace();
    }

    /**
     * Closes the error pane.
     * 
     * @param event the event that triggered the function
     */
    public void closeErrorPane(ActionEvent event) {
        errorPane.setVisible(false);
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
