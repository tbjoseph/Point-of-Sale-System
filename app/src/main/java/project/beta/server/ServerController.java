package project.beta.server;

import project.beta.BackendDAO;

public class ServerController {
    private BackendDAO dao;

    public ServerController() {

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
