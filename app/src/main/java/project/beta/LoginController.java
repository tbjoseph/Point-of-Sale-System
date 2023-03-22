package project.beta;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.beta.manager.ManagerController;
import project.beta.server.ServerHomeController;
import project.beta.types.OrderView;

import java.io.IOException;
import java.sql.SQLException;

/**
 * LoginController is the controller for the login screen.
 * It handles access to the manager and server screens.
 * 
 * @author Griffith Thomas
 */
public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorText;
    private BackendDAO dao;

    /**
     * A default constructor for the controller. Initialization is done in the
     * initialize method.
     */
    public LoginController() {
    }

    /**
     * Initialize the controller. This should only be called once,
     * as it will create a new DAO if one does not exist.
     */
    public void initialize() {
        if (dao == null) {
            try {
                dao = new BackendDAO();
            } catch (SQLException e) {
                errorText.setText(
                        "Could not connect to database, please test your network connection and restart the application.");
            } catch (RuntimeException e) {
                errorText.setText(
                        "Database credentials are missing, please configure the environment and restart the application.");
            }
        }
    }

    /**
     * Try to login with the form data
     * 
     * @param event the event that triggered this method
     * @throws IOException  if the FXML file cannot be found
     * @throws SQLException if the database cannot be accessed
     */
    public void tryLogin(ActionEvent event) throws IOException, SQLException {
        // If the DAO is null, we cannot login, but errorText should already be set
        if (dao == null) {
            return;
        }
        String username = this.username.getText();
        String password = this.password.getText();

        // Login and check the permission level
        if (dao.login(username, password)) {
            String permissionLevel = dao.getPermission(username);

            // If the permission level is valid, load the appropriate screen
            if (permissionLevel.equals("Employee")) {
                // Employee is the same as server
                FXMLLoader loader = new FXMLLoader(getClass().getResource("server/home.fxml"));
                Parent root = loader.load();
                ServerHomeController serverController = loader.getController();
                serverController.setDAO(dao);
                serverController.setOrders(new OrderView());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("common.css").toExternalForm());
                stage.setScene(scene);
                stage.setFullScreen(true);

                stage.setFullScreen(true);
                stage.show();
                return;
            } else if (permissionLevel.equals("Manager")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("manager.fxml"));
                Parent root = loader.load();
                ManagerController managerController = loader.getController();
                managerController.setDAO(dao);
                managerController.initTable();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("common.css").toExternalForm());
                scene.getStylesheets().add(getClass().getResource("manager.css").toExternalForm());
                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();
                return;
            }

            // If the permission level is invalid, print an error
            System.err.println("Unknown permission level: " + permissionLevel);
        }

        // If the login failed, clear the form and print an error
        this.username.setText("");
        this.password.setText("");
        this.errorText.setText("Invalid username or password");
    }

    /**
     * Set the DAO to use for this controller.
     * Mainly used for testing
     * 
     * @param dao the DAO to use for this controller
     */
    public void setDAO(BackendDAO dao) {
        this.dao = dao;
    }
}
