package project.beta.server;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class ServerHomeController {
    public void buttonPress(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String buttonName = sourceButton.getText();

        System.out.println(buttonName + " button pressed");
    }

    public void initialize() {
        System.out.println("Hello, world!");
    }
}
