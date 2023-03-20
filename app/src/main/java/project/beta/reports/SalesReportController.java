package project.beta.reports;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// FIXME:
import java.io.*;

import project.beta.BackendDAO;

public class SalesReportController {
    private BackendDAO dao;
    private Timestamp startDate;
    private Timestamp endDate;

    public void initialize() {

    }

    public void generateReport() {
        // TODO:
        try {
            ArrayList<Long> orderIDs = dao.getOrderIDs(startDate, endDate);
            for (long orderID : orderIDs) {
                System.out.println(orderID + ":");

                ArrayList<Long> menuIDs = dao.getOrderMenuIDs(orderID);
                for (long menuID : menuIDs) {
                    String name = dao.getMenuItemByID(menuID);
                    System.out.println("    " + name);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
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
