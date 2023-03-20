package project.beta.reports;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
            ArrayList<String> inventoryNames = dao.getSalesData(startDate, endDate);

            HashMap<String, Integer> uniqueNames = new HashMap<>();
            for (String name : inventoryNames) {
                if (uniqueNames.containsKey(name)) {
                    uniqueNames.put(name, uniqueNames.get(name) + 1);
                } else {
                    uniqueNames.put(name, 1);
                }
            }

            for (HashMap.Entry<String, Integer> entry : uniqueNames.entrySet()) {
                System.out.println(entry.getValue().toString() + ' ' + entry.getKey());
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
