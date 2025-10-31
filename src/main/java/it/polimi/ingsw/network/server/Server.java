package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Server
 */
public class Server {
    private final GameController gameController;

    /**
     * Create new server
     * @param gameController the game controller
     */
    public Server(GameController gameController) {
        this.gameController = gameController;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Porta del server?");
        int socketPort = Integer.parseInt(scanner.nextLine());

        ServerSocket socket;
        try {
            socket = new ServerSocket(socketPort);
        } catch (IOException e) {
            System.out.println("Non è possibile aprire un server socket");
            System.exit(1);
            return;
        }

        while (true) {
            try {
                Socket client = socket.accept();
                ClientHandler clientHandler = new ClientHandler(client, this);
                Thread thread = new Thread(clientHandler, "server_" + client.getInetAddress());
                thread.start();
                if (gameController.isLobbyFill()) {
                    socket.close();
                    break;
                }
            } catch (IOException e) {
                System.out.println("Connessione interrotta");
            }
        }
    }

    /**
     * Call the game controller
     * @param message the message received from a client
     */
    public void onMessage(Message message) {
        gameController.onMessage(message);
    }

    /**
     * Check if the username is valid
     * @param username new selected username
     * @return true if the username has not already been chosen by another player
     */
    public boolean usernameIsValid(String username) {
        return gameController.checkUsername(username);
    }

    /**
     * Call the game controller to connect a new player
     * @param username the new player username
     * @param clientHandler the client handler from new player
     */
    public void login(String username, ClientHandler clientHandler) {
        gameController.login(username, new VirtualView(clientHandler));
    }

    /**
     * Method to tell the game controller that a player is logged out
     */
    public void onDisconnect(){
        gameController.disconnect();
    }

}
