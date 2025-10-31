package it.polimi.ingsw.view.CLI;

import com.google.gson.Gson;
import it.polimi.ingsw.enumerations.StartingScreenState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.view.CLI.Screens.*;
import it.polimi.ingsw.view.interfaces.View;

/**
 * Class that manage the interaction with the player.
 */
public class CLI implements View, Runnable {
    private ClientSocket ClientSocket;
    private Screen nextScreen;
    private Screen currentScreen;
    private boolean shouldStop;
    private String username;
    private Game game;


    public Game getGame() {return game;}
    public ClientSocket getClientSocket(){
        return this.ClientSocket;
    }
    public void setClientSocket(ClientSocket ClientSocket){
        this.ClientSocket = ClientSocket;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Method to stop the CLI
     */
    public void stop() {
        this.shouldStop = true;
        currentScreen.stopScreen();
    }

    /**
     * Method that manage the GUI. When it starts he asks the player the IP address and the port number to connect
     * to the server and try to create the connection. When the connection is created it starts the Screen state machine
     * that show the player a different screen based on the action he has to perform.
     */
    @Override
    public void run() {
        //Avvio la macchina a stati delle schermate
        nextScreen = new LoadingScreen();
        runScreenStateMachine();
    }

    /**
     * Method that represent the Screen state machine.
     */
    private void runScreenStateMachine(){
        boolean stop;
        stop = shouldStop;

        while(!stop){
            if(currentScreen == null){
                currentScreen = new LoadingScreen();
            }
            currentScreen.setOwner(this);
            currentScreen.run();

            synchronized (this){
                stop = shouldStop;
                currentScreen = nextScreen;
                nextScreen = null;
            }
        }
    }

    /**
     * method to change the Screen showed to the player
     * @param newScreen the next Screen to show
     */
    private void changeScreen(Screen newScreen){
        synchronized (this) {
            nextScreen = newScreen;
            currentScreen.stopScreen();
        }
    }

    @Override
    public void connect(boolean firstAttempt){
        changeScreen(new StartingScreen(StartingScreenState.ASK_USERNAME, firstAttempt));
    }

    @Override
    public void getGameMode() {
        changeScreen(new StartingScreen(StartingScreenState.ASK_GAME_MODE));
    }

    @Override
    public void getPlayersNumber() {
        changeScreen(new StartingScreen(StartingScreenState.ASK_PLAYERS_NUMBER));
    }

    @Override
    public void startGame(String jsonModel) {
        synchronized (this) {
            game = new Gson().fromJson(jsonModel, Game.class);
            changeScreen(new PlanningScreen());
        }
    }

    @Override
    public void gameModel(String jsonModel) {
        game = new Gson().fromJson(jsonModel, Game.class);
    }

    @Override
    public void printString(String message) {
    }

    /**
     * method to tell the player that it's his turn to choose an assistant card to play
     */
    @Override
    public void askAssistantCard() {
            if (currentScreen.getClass() == PlanningScreen.class) {
                PlanningScreen p = (PlanningScreen) currentScreen;
                p.beginTurn();
            } else {
                PlanningScreen p = new PlanningScreen();
                p.beginTurn();
                this.changeScreen(p);
            }
    }

    @Override
    public void askStudentMovement() {
        if (currentScreen.getClass() == ActionScreen.class){
            ActionScreen s = (ActionScreen) currentScreen;
            s.placeStudent();
        } else {
            ActionScreen s = new ActionScreen();
            this.changeScreen(s);
            s.placeStudent();
        }
    }

    @Override
    public void askMotherNatureMovement() {
        ActionScreen s = (ActionScreen)this.currentScreen;
        s.moveMotherNature();
    }

    @Override
    public void askCloudChoice() {
        ActionScreen s = (ActionScreen)this.currentScreen;
        s.chooseCloud();
    }

    /**
     * Method to tell the player that he has won the game and to show the correct Screen
     */
    @Override
    public void win() {
        this.changeScreen(new EndGameScreen(true));
    }

    /**
     * Method to tell the player that he has lost the game and show the correct Screen
     */
    @Override
    public void loss() {
        this.changeScreen(new EndGameScreen(false));
    }

}
