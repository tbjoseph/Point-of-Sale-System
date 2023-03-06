package project.beta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class BackendDAO {
    private Connection connection;

    /**
     * Default constructor for BackendDAO.
     * Requires the environment variables PSQL_USER and PSQL_PASS to be set to
     * login.
     */
    public BackendDAO() {
        String username = System.getenv("PSQL_USER");
        String password = System.getenv("PSQL_PASS");

        // if we don't have the environment variables, don't try to connect
        if (username == null || password == null) {
            System.err.println("PSQL_USER and PSQL_PASS environment variables must be set");
            connection = null;
            return;
        }

        // create connection
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_beta",
                    username, password);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    /**
     * Executes a query and returns the ResultSet
     * 
     * @param query - query to execute
     * @return ResultSet of the query
     */
    public ResultSet executeQuery(String query) {
        try {
            java.sql.Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        return null;
    }

    /**
     * Checks if the username and password are valid
     * 
     * @param username - username to check
     * @param password - password to check
     * @return true if the username and password are valid, false otherwise
     */
    public boolean login(String username, String password) {
        // TODO: implement this
        if (username == null || password == null) {
            return false;
        }
        return username.equals("admin") && password.equals("admin");
    }
}
