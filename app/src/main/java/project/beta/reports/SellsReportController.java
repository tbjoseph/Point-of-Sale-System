package project.beta.reports;

import java.sql.Timestamp;
import java.time.format.DateTimeParseException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import project.beta.BackendDAO;

/**
 * Controller for What Sells Together
 * 
 * (Teams of 5+ Only) Given a time window, display a list of pairs of menu items
 * that sell together often, popular or not, sorted by most frequent.
 */
public class SellsReportController {
    private BackendDAO dao;
    private Timestamp startDate;
    private Timestamp endDate;

    @FXML
    private VBox errorPane;
    @FXML
    private Label errorText;

    private void generateReport() {

    }

    /**
     * Pass down the DAO to use for this controller
     * 
     * @param dao the DAO to use for this controller
     */
    public void setDAO(BackendDAO dao) {
        this.dao = dao;
    }

    public void setInputs(Timestamp start, Timestamp end) {
        this.startDate = start;
        this.endDate = end;
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
