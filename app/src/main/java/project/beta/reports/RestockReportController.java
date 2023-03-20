package project.beta.reports;

import project.beta.BackendDAO;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;

public class RestockReportController {
    private BackendDAO dao;
    @FXML
    private Label errorText;

    public void initialize() {
        try {
            ResultSet inventory = dao.getInventoryItems();
        } catch (SQLException e) {

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
}
