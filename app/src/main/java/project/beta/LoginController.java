package project.beta;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.beta.server.ServerController;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Label errorText;
    private BackendDAO dao;

    public void initialize() {
        if (dao == null) {
            dao = new BackendDAO();
        }
    }

    /**
     * Try to login with the form data
     * 
     * @param event - the event that triggered this method
     * @throws IOException
     */
    public void tryLogin(ActionEvent event) throws IOException {
        String username = this.username.getText();
        String password = this.password.getText();
        if (dao.login(username, password)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("server_home.fxml"));
            Parent root = loader.load();
            ServerController serverController = loader.getController();
            serverController.setDAO(dao);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("common.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("server_home.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } else {
            this.username.setText("");
            this.password.setText("");
            this.errorText.setText("Invalid username or password");
        }
    }

    /**
     * Set the DAO to use for this controller.
     * Mainly used for testing
     * 
     * @param dao - the DAO to use for this controller
     */
    public void setDAO(BackendDAO dao) {
        this.dao = dao;
    }
}
