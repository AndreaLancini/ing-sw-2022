package it.polimi.ingsw.view.CLI.Screens;

import it.polimi.ingsw.enumerations.StartingScreenState;
import it.polimi.ingsw.network.messages.*;

import java.io.IOException;
import java.util.Scanner;

/**
 * Screen to show while the client is waiting for messages from the server
 */
public class StartingScreen extends Screen{


    private final StartingScreenState state;
    private final boolean error;

    public StartingScreen(StartingScreenState state, boolean error) {
        this.state = state;
        this.error = error;
    }

    public StartingScreen(StartingScreenState state) {
        this.state = state;
        this.error = true;
    }

    /**
     * method to show the waiting screen
     */
    @Override
    public void run() {
        switch (this.state){
            case ASK_USERNAME:
                if(this.error)
                    System.out.println("Username inserito già utilizzato, inseriscine uno nuovo");
                String username = this.getUsername();
                owner.getClientSocket().sendResponse(new LoginMessage(username));
                owner.setUsername(username);

                break;
            case ASK_GAME_MODE:

                boolean gameMode = this.getGameMode();

                owner.getClientSocket().sendResponse(new Difficult(owner.getUsername(), gameMode));


                break;
            case ASK_PLAYERS_NUMBER:

                int playersNumber = this.getPlayersNumber();

                 owner.getClientSocket().sendResponse(new PlayerNumber(owner.getUsername(), playersNumber));
                break;
        }
    }

    /**
     * Method to ask the player the IP address of the server
     * @return a String containing the IP address of the server
     */
    private String getIP() {
        System.out.println("Inserisci l'IP del server");
        String ip = input.nextLine();
        while(!correctIp(ip)){
            System.out.println("IP non valido, riprova");
            ip = input.nextLine();
        }
        return ip;
    }

    /**
     * Method to check if the IP typed by the player is a correct IP address
     * @param ip the address typed by the player
     * @return true if the IP address is correct, false otherwise
     */
    public boolean correctIp(String ip){
        String[] num = ip.split("\\.");
        if(num.length != 4)
            return false;
        for (String s : num) {
            if (Integer.parseInt(s) < 0 || Integer.parseInt(s) > 255)
                return false;
        }
        return true;
    }

    /**
     * Method to ask the player the port number of the server
     * @return the port address of the server
     */
    public int getPortNumber() {
        System.out.println("Inserisci il numero di porta del server");
        int portNumber = input.nextInt();
        while(portNumber < 0 || portNumber > 65535){
            System.out.println("Numero di porta non valido, riprova");
            portNumber = input.nextInt();
        }
        return portNumber;
    }

    /**
     * Method to ask the player the username he wants to use
     * @return a String containing the username of the player
     */
    private String getUsername() {
        System.out.println("Inserisci il tuo username");
        return input.nextLine();
    }

    /**
     * Method to ask the player the game mode he wants to play
     * @return true if the player wants to play an expert game, false otherwise
     */
    public boolean getGameMode() {
        System.out.println("Scegli la modalità di gioco(1-Normale, 2-Esperto)");
        int gameMode = input.nextInt();
        while(gameMode != 1 && gameMode != 2){
            System.out.println("Scelta non valida, riprova(1-Normale, 2-Esperto)");
            gameMode = input.nextInt();
        }
        return gameMode != 1;
    }

    /**
     * Method to ask the number of players of the game
     * @return the number of players chosen by the player
     */
    public int getPlayersNumber() {
        System.out.println("Scegli il numero di giocatori(2 o 3)");
        int playerNumber = input.nextInt();
        while(playerNumber != 2 && playerNumber != 3){
            System.out.println("Scelta non valida, riprova(2 o 3)");
            playerNumber = input.nextInt();
        }
        return playerNumber;
    }
}

