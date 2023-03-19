package project.beta.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.SQLException;
import javafx.util.Pair;
import javafx.scene.layout.HBox;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;

import project.beta.BackendDAO;

public class ExcessReportController {
    private BackendDAO dao;
    private Timestamp timestamp;
    private HashMap<Pair<String, Long>, Long> inventory_data;
    private ArrayList<String> excessList;

    @FXML
    private HBox excessCont;

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

    public void setInput(Timestamp timestamp) {
        this.timestamp = timestamp;

        try {
            inventory_data = dao.construct_inventory_data(this.timestamp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}



/*
 * Excess Report notes
 * 
 * Use SQL query that gets item names and their current inventory stock and use it 
 * to create a Hash with total inventory sold for each item
 * 
 *          SELECT i.item_name,i.quantity
 *          FROM order_history o 
 *          JOIN order_menu_assoc a ON a.order_id = o.id 
 *          JOIN menu_items m ON m.id = a.menu_item_id 
 *          JOIN menu_inventory_assoc b ON b.menu_item_id = m.id 
 *          JOIN inventory_items i ON i.inventory_id = b.inventory_item_id
 *          WHERE o.order_date > ?;
 * 
 * Use SQL query to find if there is an order with a date greater than the date 
 * specified to check for restocks for a given item
 */