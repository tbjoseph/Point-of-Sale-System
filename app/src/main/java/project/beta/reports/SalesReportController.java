package project.beta.reports;

import java.sql.Timestamp;

import project.beta.BackendDAO;

public class SalesReportController {
    private BackendDAO dao;
    private Timestamp startDate;
    private Timestamp endDate;

    public void initialize() {

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
}
