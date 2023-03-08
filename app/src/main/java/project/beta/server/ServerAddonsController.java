package project.beta.server;

import javafx.event.ActionEvent;
import project.beta.BackendDAO;
import javafx.scene.control.Button;
import project.beta.server.OrderView;

public class ServerAddonsController {
    private BackendDAO dao;

    public void addAppetizer(ActionEvent event) {
        Button bt = (Button) event.getSource();
        String name = bt.getText();
        System.out.println(name);
    }

    public void addDrink(ActionEvent event) {
        Button bt = (Button) event.getSource();
        String name = bt.getText();

    }

    public ServerAddonsController() {

    }

    /**
     * Pass down the DAO to use for this controller
     * 
     * @param dao - the DAO to use for this controller
     */
    public void setDAO(BackendDAO dao) {
        this.dao = dao;
    }
}
