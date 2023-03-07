package project.beta.server;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class ServerHomeController {
    /**
     * Add a side item to the current order.
     * 
     * @param event Used to get the button name of the side item.
     */
    public void addSide(ActionEvent event) {
        Button sideButton = (Button) event.getSource();
        String sideName = sideButton.getText();

        // TODO:
        // add the side to the order depending on the mode
        // a la carte, bowl, plate, etc.
        System.out.println(sideName + " button pressed");
    }

    /**
     * Add an entree to the current order.
     * 
     * @param event Used to get the button name of the entree.
     */
    public void addEntree(ActionEvent event) {
        Button entreeButton = (Button) event.getSource();
        String entreeName = entreeButton.getText();

        // TODO:
        // add the entree to the order depending on the mode
        // a la carte, bowl, plate, etc.
        System.out.println(entreeName + " button pressed");
    }

    public void nextScreen(ActionEvent event) {
        // TODO:
        // change the scene to the drinks and appetizer screen
        // pass the current order to the next screen
    }

    public void initialize() {
        System.out.println("Hello, world!");
    }
}
