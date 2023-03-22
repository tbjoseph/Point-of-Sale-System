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
import java.sql.SQLException;
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
    DatePicker sellsStartDatePicker;
    @FXML
    TextField sellsStartTimePicker;
    @FXML
    DatePicker sellsEndDatePicker;
    @FXML
    TextField sellsEndTimePicker;

    @FXML
    DatePicker timestampDatePicker;
    @FXML
    TextField timestampTimePicker;

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

        Timestamp start = parseTimestamp(salesStartDatePicker.getValue(), salesStartTimePicker.getText());
        Timestamp end = parseTimestamp(salesEndDatePicker.getValue(), salesEndTimePicker.getText());
        controller.setInputs(start, end);

        // generate sales report after DAO and timestamps are passed down
        try {
            controller.setupSalesReport();
            controller.displayReport();
        } catch (SQLException e) {
            handleError(e);
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
        // get the sales controller and load it
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sales_report.fxml"));
        Parent root = loader.load();
        SalesReportController controller = loader.getController();
        controller.setDAO(dao);

        // generate sales report after DAO and timestamps are passed down
        try {
            controller.setupXReport();
            controller.displayReport();
        } catch (SQLException e) {
            handleError(e);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Generate a Z report
     * 
     * @param event the event that triggered the function
     * @throws IOException if the file is not found
     */
    public void generateZReport(ActionEvent event) throws IOException {
        // get the sales controller and load it
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sales_report.fxml"));
        Parent root = loader.load();
        SalesReportController controller = loader.getController();
        controller.setDAO(dao);

        // generate sales report after DAO and timestamps are passed down
        try {
            controller.setupZReport();
            controller.displayReport();
        } catch (SQLException e) {
            handleError(e);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Generate an excess report
     * 
     * @param event the event that triggered the function
     * @throws IOException if the file is not found
     */
    public void generateExcessReport(ActionEvent event) throws IOException {
        // get the sales controller and load it
        FXMLLoader loader = new FXMLLoader(getClass().getResource("excess_report.fxml"));
        Parent root = loader.load();
        ExcessReportController controller = loader.getController();
        controller.setDAO(dao);

        try {
            // set any necessary data from the inputs
            LocalDate date = timestampDatePicker.getValue();
            String time = timestampTimePicker.getText();
            // prepend a 0 if the hour is only one digit
            if (time.split(":")[0].length() == 1) {
                time = "0" + time;
            }
            LocalTime time2 = LocalTime.parse(time);
            Timestamp timestamp = Timestamp.valueOf(date.atTime(time2));
            controller.setInput(timestamp);
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
     * Generate a restock report
     * 
     * @param event the event that triggered the function
     * @throws IOException if the file is not found
     */
    public void generateRestockReport(ActionEvent event) throws IOException {
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
        // get the sales controller and load it
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sells_report.fxml"));
        Parent root = loader.load();
        SellsReportController controller = loader.getController();
        controller.setDAO(dao);

        Timestamp start = parseTimestamp(sellsStartDatePicker.getValue(), sellsStartTimePicker.getText());
        Timestamp end = parseTimestamp(sellsEndDatePicker.getValue(), sellsEndTimePicker.getText());
        controller.setInputs(start, end);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("sells_report.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
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
     * Handles a DateTimeParseException by printing the error to the console and
     * setting the errorText label.
     * 
     * @param exception the exception to handle
     */
    private void handleError(DateTimeParseException exception) {
        errorPane.setVisible(true);
        errorText.textProperty().set("Please enter a valid date.");
        exception.printStackTrace();
    }

    /**
     * Handles a SQLException by printing the error to the console and setting the
     * errorText label.
     * 
     * @param exception - the exception to handle
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
     * Parse a LocalDate and a String into a Timestamp
     * 
     * @param date the date
     * @param time the time
     * @return a Timestamp made from the date and time
     */
    private Timestamp parseTimestamp(LocalDate date, String time) {
        try {
            if (time.strip().equals("")) {
                time = "00:00";
            }
            // prepend a 0 if the hour is only one digit
            if (time.split(":")[0].length() == 1) {
                time = "0" + time;
            }
            LocalTime time2 = LocalTime.parse(time);
            return Timestamp.valueOf(date.atTime(time2));
        } catch (DateTimeParseException e) {
            handleError(e);
            return null;
        }
    }
}
