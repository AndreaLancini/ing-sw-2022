package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;


/**
 * Client main
 */
public class ClientMain {

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
