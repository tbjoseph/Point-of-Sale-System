package project.beta.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.SQLException;
import javafx.util.Pair;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;

import project.beta.BackendDAO;


public class ExcessReportController {
    private BackendDAO dao;
    private Timestamp timestamp;
    private HashMap<Pair<String, Long>, Long> inventory_data;
    private ArrayList<String> excessList;

    // @FXML
    // private HBox excessCont;

    @FXML
    private TextArea textArea;

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
            output_excess();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void output_excess() throws SQLException {
        excessList = new ArrayList<>();

        for(Pair<String, Long> item : inventory_data.keySet()) {


            if (dao.restock(item.getKey(), timestamp)) continue;

            Long inventory_sold = inventory_data.get(item);
            double excess_ratio = (double) inventory_sold / (double) ( item.getValue() + inventory_sold );
            
            if (excess_ratio > 0.1) continue;
            
            excessList.add(item.getKey());
            
        }

        String joinedStrings = String.join("\n", excessList);
        textArea.setText(joinedStrings);
    }

    /**
     * Post checkout function to get to home
     * 
     * @param event the event that triggered the function
     */
    public void goToHome(ActionEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Parent root = loader.load();

            ReportsHomeController serverController = loader.getController();
            serverController.setDAO(dao);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("../common.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

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