package project.beta;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * MainApp is the main class for the JavaFX application.
 * 
 * @author Griffith Thomas
 */
public class MainApp extends Application {
    /**
     * A default constructor for the application. We don't need any extra state here
     */
    public MainApp() {
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = null;

        // Load the FXML file
        root = FXMLLoader.load(getClass().getResource("login.fxml"));

        // Create the scene and set the stage
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("common.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());

        Image icon = new Image(getClass().getResourceAsStream("icon.png"));
        stage.getIcons().add(icon);
        stage.setTitle("JavaFX and Gradle");
        stage.setScene(scene);

        // Uncomment to go full screen
        // stage.setFullScreen(true);
        // stage.setFullScreenExitHint("Press ESC to exit full screen");
        stage.show();
    }

    /**
     * Main method for the application.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
