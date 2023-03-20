package project.beta.reports;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import project.beta.BackendDAO;

/**
 * Controller class for the reports home screen.
 * 
 * @author Griffith Thomas
 */
public class ReportsHomeController {
    private BackendDAO dao;

    @FXML
    DatePicker salesStartDatePicker;
    @FXML
    TextField salesStartTimePicker;
    @FXML
    DatePicker salesEndDatePicker;
    @FXML
    TextField salesEndTimePicker;

    @FXML
    DatePicker timestampDatePicker;
    // @FXML

    @FXML
    private VBox errorPane;
    @FXML
    private Label errorText;

    /**
     * Constructor for ReportsHomeController. Initialization is done in initialize()
     */
    public ReportsHomeController() {
    }

    /**
     * Initialize the controller
     */
    public void initialize() {
        errorPane.setVisible(false);
    }

    /**
     * Generate a sales report
     * 
     * @param event the event that triggered the function
     * @throws IOException if the file is not found
     */
    public void generateSalesReport(ActionEvent event) throws IOException {
        // get the sales controller and load it
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sales_report.fxml"));
        Parent root = loader.load();
        SalesReportController controller = loader.getController();
        controller.setDAO(dao);

        try {
            // set any necessary data from the inputs
            LocalDate date = salesStartDatePicker.getValue();
            String time = salesStartTimePicker.getText();
            // prepend a 0 if the hour is only one digit
            if (time.split(":")[0].length() == 1) {
                time = "0" + time;
            }
            LocalTime time2 = LocalTime.parse(time);
            Timestamp start = Timestamp.valueOf(date.atTime(time2));
            date = salesEndDatePicker.getValue();
            time = salesEndTimePicker.getText();
            if (time.split(":")[0].length() == 1) {
                time = "0" + time;
            }
            time2 = LocalTime.parse(time);
            Timestamp end = Timestamp.valueOf(date.atTime(time2));
            controller.setInputs(start, end);
        } catch (DateTimeParseException e) {
            handleError(e);
            return;
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Generate an X report
     * 
     * @param event the event that triggered the function
     * @throws IOException if the file is not found
     */
    public void generateXReport(ActionEvent event) throws IOException {
        // TODO
    }

    /**
     * Generate a Z report
     * 
     * @param event the event that triggered the function
     * @throws IOException if the file is not found
     */
    public void generateZReport(ActionEvent event) throws IOException {
        // TODO
    }

    /**
     * Generate an excess report
     * 
     * @param event the event that triggered the function
     * @throws IOException if the file is not found
     */
    public void generateExcessReport(ActionEvent event) throws IOException {
        // TODO
    }

    /**
     * Generate a restock report
     * 
     * @param event the event that triggered the function
     * @throws IOException if the file is not found
     */
    public void generateRestockReport(ActionEvent event) throws IOException {
        // TODO
        // get the sales controller and load it
        FXMLLoader loader = new FXMLLoader(getClass().getResource("restock_report.fxml"));
        Parent root = loader.load();
        RestockReportController controller = loader.getController();
        controller.setDAO(dao);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("restock_report.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Generate a what sells together report
     * 
     * @param event the event that triggered the function
     * @throws IOException if the file is not found
     */
    public void generateSellsTogetherReport(ActionEvent event) throws IOException {
        // TODO
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
     * Handles a SQLException by printing the error to the console and setting the
     * errorText label.
     * 
     * @param exception - the exception to handle
     */
    private void handleError(DateTimeParseException exception) {
        errorPane.setVisible(true);
        errorText.textProperty().set("Warning: " + exception.getMessage());
        exception.printStackTrace();
    }

    /**
     * Closes the error pane.
     * 
     * @param event - the event that triggered the function
     */
    public void closeErrorPane(ActionEvent event) {
        errorPane.setVisible(false);
    }
}
