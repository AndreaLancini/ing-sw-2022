package it.polimi.ingsw.view;

import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.view.interfaces.View;

import java.io.IOException;

public class VirtualView implements View {
    private final ClientHandler clientHandler;

    /**
     * Default constructor
     * @param clientHandler the client handler that communicate with the virtual view.
     */
    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    /**
     * Returns the client handler associated to a client
     * @return client handler
     */
    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    @Override
    public void connect(boolean firstAttempt) {}

    @Override
    public void getGameMode() {
        try {
            clientHandler.sendMessage(new ServerMessage(Request.DIFFICULT));
        } catch (IOException e) {
            System.out.println("Errore nell'inviare un messaggio");
        }
    }

    @Override
    public void getPlayersNumber() {
        try {
            clientHandler.sendMessage(new ServerMessage(Request.PLAYERNUMBER));
        } catch (IOException e) {
            System.out.println("Errore nell'inviare un messaggio");
        }
    }

    @Override
    public void startGame(String jsonModel) {
        try {
            clientHandler.sendMessage(new StartGameMessage(jsonModel));
        } catch (IOException e) {
            System.out.println("Errore nell'inviare un messaggio");
        }
    }

    @Override
    public void askAssistantCard() {
        try {
            clientHandler.sendMessage(new ServerMessage(Request.PLAY_ASSISTANT_CARD));
        } catch (IOException e) {
            System.out.println("Errore nell'inviare un messaggio");
        }
    }

    @Override
    public void askStudentMovement() {
        try {
            clientHandler.sendMessage(new ServerMessage(Request.MOVE_STUDENT));
        } catch (IOException e) {
            System.out.println("Errore nell'inviare un messaggio");
        }
    }

    @Override
    public void askMotherNatureMovement() {
        try {
            clientHandler.sendMessage(new ServerMessage(Request.MOVE_MOTHER_NATURE));
        } catch (IOException e) {
            System.out.println("Errore nell'inviare un messaggio");
        }
    }

    @Override
    public void askCloudChoice() {
        try {
            clientHandler.sendMessage(new ServerMessage(Request.CHOOSE_CLOUD));
        } catch (IOException e) {
            System.out.println("Errore nell'inviare un messaggio");
        }
    }

    @Override
    public void win() {
        try {
            clientHandler.sendMessage(new ServerMessage(Request.WIN));
        } catch (IOException e) {
            System.out.println("Errore nell'inviare un messaggio");
        }
    }

    @Override
    public void loss() {
        try {
            clientHandler.sendMessage(new ServerMessage(Request.LOSE));
        } catch (IOException e) {
            System.out.println("Errore nell'inviare un messaggio");
        }
    }

    @Override
    public void gameModel(String jsonModel){
        try {
            clientHandler.sendMessage(new ModelMessage(jsonModel));
        } catch (IOException e) {
            System.out.println("Errore nell'inviare un messaggio");
        }
    }

    @Override
    public void printString(String message){
        try {
            clientHandler.sendMessage(new StringMessage(message));
        } catch (IOException ignored) {}
    }
}
