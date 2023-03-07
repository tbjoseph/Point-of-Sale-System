package project.beta.server;

import javax.swing.RootPaneContainer;

import javafx.scene.control.Button;
import project.beta.BackendDAO;

public class ServerAddonsController {
    private BackendDAO dao;

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
