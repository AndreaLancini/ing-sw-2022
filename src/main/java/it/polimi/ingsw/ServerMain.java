package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.server.Server;

/**
 * Server main
 */
public class ServerMain {

    public static void main(String[] args) {
        GameController gameController = new GameController();
        Server server = new Server(gameController);
        server.run();
    }
}
