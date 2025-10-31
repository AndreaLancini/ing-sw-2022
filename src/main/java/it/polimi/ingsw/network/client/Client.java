package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.CLI.CLI;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client
 */
public class Client implements Runnable {
    private ClientSocket clientSocket;
    private CLI cli;

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        Socket server = null;

        do {
            System.out.println("Indirizzo IP del server?");
            String ip = scanner.nextLine();
            System.out.println("Porta del server?");
            int socketPort = Integer.parseInt(scanner.nextLine());
            try {
                server = new Socket(ip, socketPort);
            } catch (IOException e) {
                System.out.println("Server non raggiungibile, controlla i dati inseriti!");
            }
        }while (server == null);
        clientSocket = new ClientSocket(server, this);
        Thread serverHandlerThread = new Thread(clientSocket, "server_" + server.getInetAddress().getHostAddress());
        serverHandlerThread.start();

        cli = new CLI();
        cli.setClientSocket(clientSocket);
        cli.run();
    }

    /**
     * Method that perform an action based on the message received from server
     * @param message the message received
     */
    public void onMessage(Message message) {
        switch (message.getMessageType()) {
            case LOGIN:
                cli.connect(((ServerMessage) message).isFirstAttempt());
                break;
            case PLAYERNUMBER:
                cli.getPlayersNumber();
                break;
            case DIFFICULT:
                cli.getGameMode();
                break;
            case MODEL:
                cli.gameModel(((ModelMessage) message).getJsonModel());
                break;
            case START_GAME:
                cli.startGame(((StartGameMessage) message).getJsonModel());
                break;
            case PLAY_ASSISTANT_CARD:
                cli.askAssistantCard();
                break;
            case MOVE_STUDENT:
                cli.askStudentMovement();
                break;
            case MOVE_MOTHER_NATURE:
                cli.askMotherNatureMovement();
                break;
            case CHOOSE_CLOUD:
                cli.askCloudChoice();
                break;
            case WIN:
                cli.win();
                break;
            case LOSE:
                cli.loss();
                break;
            case STRING:
                String s = ((StringMessage) message).getMessage();
                System.out.println(s);
                if(s.equals("Un giocatore si è disconnesso, la partita verrà chiusa")) {
                    cli.stop();
                    clientSocket.stop();
                }
                break;
        }
    }

}
