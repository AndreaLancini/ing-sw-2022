package it.polimi.ingsw.view.interfaces;

import java.util.ArrayList;

public interface View {
    /**
     * Asks the player the IP address and the port number of the server and sends them to the Network Handler to connect
     * @param firstAttempt tells if a previous attempt already failed to connect
     */
    void connect(boolean firstAttempt);

    /**
     * Asks the player to choose the game mode and then sends it to the Network Handler
     */
    void getGameMode();

    /**
     * Asks the player to choose the number of players of the game and then sends it to the Network Handler
     */
    void getPlayersNumber();

    /**
     * Method to tell the View that the game is starting
     * @param jsonModel the JSON string that represent the starting state of the model
     */
    void startGame(String jsonModel);

    /**
     * This method asks the player to play an assistant card
     */
    void askAssistantCard();

    /**
     * method ask the player which student to move and where to move it
     */
    void askStudentMovement();

    /**
     * method ask the player where to move mother nature
     */
    void askMotherNatureMovement();

    /**
     * method ask the player which cloud he chose
     */
    void askCloudChoice();

    /**
     * method to tell the player that he won the game
     */
    void win();

    /**
     * method to tell the player that he lost the game
     */
    void loss();

    /**
     * method update the game model of the view
     * @param jsonModel the JSON string that represent the updated model
     */
    void gameModel(String jsonModel);

    /**
     * method to print a server message
     * @param message the server message
     */
    void printString(String message);
}
