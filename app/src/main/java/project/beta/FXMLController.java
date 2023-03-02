package project.beta;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.DriverManager;

public class FXMLController {

    @FXML
    private Label header;
    @FXML
    private Label contents;
    private Connection connection;

    public void initialize() {
        String username = System.getenv("PSQL_USER");
        String password = System.getenv("PSQL_PASS");

        // check environment variables
        if (username == null || password == null) {
            System.err.println("PSQL_USER and PSQL_PASS environment variables must be set");
            System.exit(1);
        }

        // create connection
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_beta",
                    username, password);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }

        // query database
        String rows = "";
        try {
            java.sql.Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM order_history LIMIT 10");
            while (resultSet.next()) {
                rows += resultSet.getString("order_id") + ", ";
                rows += resultSet.getString("order_date") + ", ";
                rows += resultSet.getString("item_id") + ", ";
                rows += resultSet.getString("price") + "\n";
            }
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        header.setText("order_id, order_date, item_id, price");
        contents.setText(rows);
    }
}
