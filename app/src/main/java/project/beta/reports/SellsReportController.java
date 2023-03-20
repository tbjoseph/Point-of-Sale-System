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
import javafx.scene.control.cell.PropertyValueFactory;
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

    @FXML
    private TableView<Pair<String, Float>> resultsTable;
    @FXML
    private TableColumn<Pair<String, Float>, String> freqCol;
    @FXML
    private TableColumn<Pair<String, Float>, String> menuNamesCol;

    @FXML
    private VBox errorPane;
    @FXML
    private Label errorText;

    /**
     * Initializes the controller class. This method is automatically called after
     * the fxml file has been loaded.
     */
    public void initialize() {
        errorPane.setVisible(false);
        menuNamesCol.setCellValueFactory(new PropertyValueFactory<Pair<String, Float>, String>("key"));
        freqCol.setCellValueFactory(
                r -> new ReadOnlyObjectWrapper<String>(String.format("%.2f %%", r.getValue().getValue() * 100)));
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
                        // we skip combinations which are the same, or where i < j
                        // to make sure we only get each combination once
                        if (menuItems.get(i) <= menuItems.get(j)) {
                            continue;
                        }
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
            HashMap<Long, String> nameTable = new HashMap<>();
            items.iterator().forEachRemaining(m -> nameTable.put(m.getIndex(), m.name));
            // put items in the table
            for (Pair<Long, Long> pair : counts.keySet()) {
                String menuNames = nameTable.get(pair.getKey()) + ", " + nameTable.get(pair.getValue());
                Float freq = (float) counts.get(pair)
                        / (totalCounts.get(pair.getKey()) + totalCounts.get(pair.getValue()));
                resultsTable.getItems().add(new Pair<>(menuNames, freq));
            }
            resultsTable.getItems().sort((a, b) -> (int) Math.signum(b.getValue() - a.getValue()));
            // remove items that are less than 1%
            resultsTable.getItems().removeIf(p -> p.getValue() < 0.01);
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
        stage.show();
    }
}
