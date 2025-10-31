package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.enumerations.GameState;
import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.enumerations.StepState;
import it.polimi.ingsw.enumerations.TowerColor;
import it.polimi.ingsw.exceptions.WinException;
import it.polimi.ingsw.exceptions.cards.HandException;
import it.polimi.ingsw.exceptions.map.EmptyBagException;
import it.polimi.ingsw.exceptions.map.EmptyProfessorException;
import it.polimi.ingsw.exceptions.player.SchoolBoardException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.map.Island;
import it.polimi.ingsw.model.pawns.Professor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.VirtualView;

import java.util.HashMap;
import java.util.Map;

/**
 * The class represent the logic of the game
 */
public class GameController {
    private final Game game;
    private final Map<String, VirtualView> virtualViewMap;
    private GameState gameState;
    private final CharacterCardController characterCardController;
    private StepState stepState;
    private Player currentPlayer;
    private boolean lastRound = false;
    private final Gson gson = new Gson();

    /**
     * Create new game controller
     */
    public GameController(){
        this.game = Game.getInstance();
        gameState = GameState.LOGIN;
        virtualViewMap = new HashMap<>();
        stepState = null;
        characterCardController = new CharacterCardController(game, this);
    }

    private void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    private void setStepState(StepState stepState) {
        this.stepState = stepState;
    }

    /**
     * Main method that performs actions based on the received message
     * @param message received
     */
    public void onMessage(Message message){
        switch (gameState){
            case LOGIN:
                if (message.getUsername().equals(game.getPlayers().get(0).getUsername()))
                    loginState(message);
                break;
            case STARTED:
                if (message.getUsername().equals(currentPlayer.getUsername())){
                    startedState(message);
                }
                break;
            case END:
                VirtualView virtualView = virtualViewMap.get(message.getUsername());
                virtualView.printString("La partita è terminata!");
                break;
            default:
                System.out.println("Stato della partita non valido!");
                break;
        }
    }

    /**
     * Set the number of players and the difficulty if the message is from the first connected player
     * @param message received
     */
    private void loginState(Message message){
        if (message.getMessageType() == Request.PLAYERNUMBER){
            int playerNumber = ((PlayerNumber) message).getPlayerNumber();
            if (playerNumber > 1 && playerNumber < 4){
                game.setChosenMaxPlayers(playerNumber);
            } else {
                VirtualView virtualView = virtualViewMap.get(message.getUsername());
                virtualView.getPlayersNumber();
            }
        } else if (message.getMessageType() == Request.DIFFICULT){
            game.setExpert(((Difficult) message).isExpert());
        }
        else {
            VirtualView virtualView = virtualViewMap.get(message.getUsername());
            virtualView.printString("Messaggio non valido!");
        }
    }

    /**
     * Change action based on the type of message received
     * @param message received from the current player
     */
    private void startedState(Message message){
        VirtualView virtualView = virtualViewMap.get(message.getUsername());
        switch (message.getMessageType()) {
            case PLAY_ASSISTANT_CARD:
                try {
                    playAssistantCard((PlayAssistantCard) message);
                } catch (HandException e) {
                    virtualView.printString("Carta già giocata!");
                    virtualView.askAssistantCard();
                }
                break;
            case MOVE_STUDENT_DINING:
                try {
                    moveStudentToDining((MoveStudentDining) message);
                } catch (SchoolBoardException e) {
                    virtualView.printString("Non sono presenti studenti del colore scelto!");
                    virtualView.askStudentMovement();
                }
                break;
            case MOVE_STUDENT_ISLAND:
                try {
                    moveStudentToIsland((MoveStudentIsland) message);
                } catch (SchoolBoardException e) {
                    virtualView.printString("Non sono presenti studenti del colore scelto!");
                    virtualView.askStudentMovement();
                }
                break;
            case MOVE_MOTHER_NATURE:
                if (checkMotherNatureValue((MoveMotherNature) message))
                    moveMotherNature((MoveMotherNature) message);
                else {
                    virtualView.printString("Numero di posizioni maggiore del massimo consentito!");
                    virtualView.askMotherNatureMovement();
                }
                break;
            case CHOOSE_CLOUD:
                try {
                    ChoseCloud cloudMessage = (ChoseCloud) message;
                    if (game.getPanel().getClouds().get(cloudMessage.getCloud()).getStudents().size() == 0){
                        virtualView.printString("Nuvola già scelta da un altro giocatore!");
                        virtualView.askCloudChoice();
                    } else {
                        chooseCloud(cloudMessage);
                    }
                } catch (SchoolBoardException ignored) {}
                break;
            case PLAY_CHARACTER_CARD:
                try {
                    characterCardController.onMessage((PlayCharacterCard) message);
                    sendGame();
                } catch (WinException e){
                    win(e.getPlayer());
                }
                if (game.getPanel().getIslands().size() <= 3)
                    checkWinner();
                break;
            default:
                System.out.println("Tipo di messaggio non corretto!");
                break;
        }
    }

    /**
     * Set the next player or if he is the last one starts a new phase
     */
    private void nextPlayer(){
        int index = game.getPlayers().indexOf(currentPlayer);
        if (index < game.getChosenPlayersNumber() - 1) {
            currentPlayer = game.getPlayers().get(index + 1);
            if (stepState == StepState.PLAY_ASSISTANT_CARD){
                VirtualView virtualView = virtualViewMap.get(currentPlayer.getUsername());
                virtualView.askAssistantCard();
            }
            else if (stepState == StepState.CHOOSE_CLOUD) {
                stepState = StepState.MOVE_STUDENT_1;
                VirtualView virtualView = virtualViewMap.get(currentPlayer.getUsername());
                virtualView.askStudentMovement();
            }
        }
        else
            nextPhase();
    }

    /**
     * Set the next phase based on the current and ask the player for his next action
     */
    private void nextPhase(){
        VirtualView virtualView = virtualViewMap.get(currentPlayer.getUsername());
        switch (stepState){
            case PLAY_ASSISTANT_CARD:
                game.sortPlayers();
                currentPlayer = game.getPlayers().get(0);
                setStepState(StepState.MOVE_STUDENT_1);
                virtualView = virtualViewMap.get(currentPlayer.getUsername());
                virtualView.askStudentMovement();
                break;
            case MOVE_STUDENT_1:
                setStepState(StepState.MOVE_STUDENT_2);
                virtualView.askStudentMovement();
                break;
            case MOVE_STUDENT_2:
                setStepState(StepState.MOVE_STUDENT_3);
                virtualView.askStudentMovement();
                break;
            case MOVE_STUDENT_3:
                if (game.getChosenPlayersNumber() == 2) {
                    setStepState(StepState.MOVE_MOTHER_NATURE);
                    virtualView.askMotherNatureMovement();
                }
                else if (game.getChosenPlayersNumber() == 3) {
                    setStepState(StepState.MOVE_STUDENT_4);
                    virtualView.askStudentMovement();
                }
                break;
            case MOVE_STUDENT_4:
                setStepState(StepState.MOVE_MOTHER_NATURE);
                virtualView.askMotherNatureMovement();
                break;
            case MOVE_MOTHER_NATURE:
                setStepState(StepState.CHOOSE_CLOUD);
                virtualView.askCloudChoice();
                break;
            case CHOOSE_CLOUD:
                if (lastRound)
                    checkWinner();
                else {
                    currentPlayer = game.getPlayers().get(0);
                    newRound();
                }
                break;
            default:
                System.out.println("Stato non valido!");
                break;
        }
    }

    /**
     * Fill the clouds then ask the current player which card he wants to play
     */
    private void newRound(){
        refillClouds();
        setStepState(StepState.PLAY_ASSISTANT_CARD);
        VirtualView virtualView = virtualViewMap.get(currentPlayer.getUsername());
        virtualView.askAssistantCard();
    }

    /**
     * Refill all clouds with the students
     */
    private void refillClouds(){
        try {
            game.getPanel().fillClouds();
            sendGame();
        } catch (EmptyBagException e){
            lastRound = true;
        }
    }

    /**
     * Play the selected assistant card if the player can
     * @param message received from the current player containing the value of the card he wants to play
     * @throws HandException if the card is already played
     */
    private void playAssistantCard(PlayAssistantCard message) throws HandException {
        Player player = game.getPlayerByUsername(message.getUsername());
        if (player.getDeck().getHand().size() != 1){
            for (Player p = game.getPlayers().get(0); p != player; p = game.getPlayers().get(game.getPlayers().indexOf(p) + 1)) { //check if another player has already played the same card
                if ((message).getValue() == p.getDeck().getDiscardPile().get(0).getValue()) {
                    VirtualView virtualView = virtualViewMap.get(message.getUsername());
                    virtualView.printString("Carta già giocata da un altro giocatore!");
                    virtualView.askAssistantCard();
                }
            }
        }
        player.getDeck().playAssistantCard(message.getValue());
        if (player.getDeck().getHand().size() == 0)
            lastRound = true;
        sendGame();
        nextPlayer();
    }

    /**
     * Move a student from the entrance to the dining room and check if the player can take a professor
     * @param message received from the current player containing the color of the student to move
     * @throws SchoolBoardException if there isn't a student of the selected color
     */
    private void moveStudentToDining(MoveStudentDining message) throws SchoolBoardException {
        Player player = game.getPlayerByUsername(message.getUsername());
        PawnColor color = message.getColor();
        if (player.getSchoolBoard().moveStudentToDining(color) && game.isExpert()){
            game.getPanel().removeCoins(1);
            player.addCoins(1);
        }
        int num = player.getSchoolBoard().checkStudentNumber(color);
        boolean max = true;
        for (Player p: game.getPlayers()){
            if (p != player){
                if (!characterCardController.isCookActive() && !(num > p.getSchoolBoard().checkStudentNumber(color)))
                    max = false;
                else if (game.isExpert() && characterCardController.isCookActive() && num < p.getSchoolBoard().checkStudentNumber(color))
                    max = false;
            }
        }
        if (max) //if the number of students of the same color is > of all others players
            takeProfessor(player, color);
        sendGame();
        nextPhase();
    }

    /**
     * Move one student from the entrance to the selected island
     * @param message received from the current player containing the color of the student to move
     * @throws SchoolBoardException if there isn't a student of the selected color
     */
    private void moveStudentToIsland(MoveStudentIsland message) throws SchoolBoardException {
        Player player = game.getPlayerByUsername(message.getUsername());
        player.getSchoolBoard().moveStudentToIsland(message.getColor(), game.getPanel().getIslands().get(message.getIsland()));
        sendGame();
        nextPhase();
    }

    /**
     * Move mother nature of selected positions, then calculate influence on arrival island and
     * if a player has the maximum influence add or replace towers, then merge adjacent islands if can
     * @param message received from the current player that contains the number of positions
     */
    private void moveMotherNature(MoveMotherNature message){
        Player player = game.getPlayerByUsername(message.getUsername());
        Island island = game.getPanel().moveMotherNature(message.getPos());
        Player playerMaxInfluence = player;
        int maxInfluence = player.calculateInfluence(island, characterCardController.isCentaurActive(), characterCardController.getColor());
        if (characterCardController.isSoldierActive())
            maxInfluence += 2;
        for (Player p: game.getPlayers()){
            if (p != player){
                int influence = p.calculateInfluence(island, characterCardController.isCentaurActive(), characterCardController.getColor());
                if (influence > maxInfluence){
                    maxInfluence = influence;
                    playerMaxInfluence = p;
                } else if (influence == maxInfluence)
                    playerMaxInfluence = null;
            }
        }
        if (playerMaxInfluence != null){ //if a player has the influence on that island > of all others players
            if (!island.isTowerPresent()){ //if on the island there isn't any tower, add new one
                try {
                    island.setTowerPresent(true);
                    playerMaxInfluence.getSchoolBoard().removeTower(1);
                    island.setTowerColor(playerMaxInfluence.getSchoolBoard().getTowerColor());
                } catch (SchoolBoardException e){
                    win(playerMaxInfluence);
                }
            }
            else { //if on the island there is already one or more towers, then replace them
                try {
                    TowerColor towerColor = island.getTowerColor();
                    for (Player p: game.getPlayers()){
                        if (p.getSchoolBoard().getTowerColor() == towerColor){
                            try {
                                p.getSchoolBoard().addTower(island.getIslandNumber());
                            } catch (SchoolBoardException ignored){}
                            break;
                        }
                    }
                    playerMaxInfluence.getSchoolBoard().removeTower(island.getIslandNumber());
                    island.setTowerColor(playerMaxInfluence.getSchoolBoard().getTowerColor());
                } catch (SchoolBoardException e){
                    win(playerMaxInfluence);
                }
            }
            if (playerMaxInfluence.getSchoolBoard().getTowerNumber() == 0)
                win(playerMaxInfluence);
        }
        game.getPanel().unifyIslands(island);
        if (game.getPanel().getIslands().size() <= 3)
            checkWinner();
        sendGame();
        nextPhase();
    }

    /**
     * Take all students from the selected cloud
     * @param message received from the current player that contains the selected cloud
     * @throws SchoolBoardException if the entrance is full
     */
    private void chooseCloud(ChoseCloud message) throws SchoolBoardException {
        Player player = game.getPlayerByUsername(message.getUsername());
        player.getSchoolBoard().takeStudentsFromCloud(game.getPanel().getClouds().get(message.getCloud()));
        if (game.isExpert())
            characterCardController.reset();
        sendGame();
        nextPlayer();
    }

    /**
     * Take the professor of the selected color
     * @param player the player who receives the professor
     * @param color the color of the professor to add
     */
    private void takeProfessor(Player player, PawnColor color){
        Professor professor;
        try { //take the professor from the center of the map
            professor = game.getPanel().removeProfessor(color);
            player.getSchoolBoard().addProfessor(professor);
        } catch (EmptyProfessorException e){ //if the professor isn't on the map, search on the players' school boards
            for (Player p: game.getPlayers()){
                if (p != player){
                    try {
                        professor = p.getSchoolBoard().removeProfessor(color);
                        player.getSchoolBoard().addProfessor(professor);
                    } catch (SchoolBoardException ignored) {}
                }
            }
        } catch (SchoolBoardException ignored){}
    }

    /**
     * Check if mother nature can perform the selected movements
     * @param message received from the current player
     * @return true if the selected number of movements is valid, false otherwise
     */
    private boolean checkMotherNatureValue(MoveMotherNature message){
        int pos = message.getPos();
        int maxMovements = game.getPlayerByUsername(message.getUsername()).getDeck().getDiscardPile().get(0).getMaxMovements();
        if (game.isExpert() && characterCardController.isWizardActive())
            maxMovements += 2;
        return pos <= maxMovements;
    }

    /**
     * Check who is the winner
     */
    private void checkWinner(){
        Player winner = game.getPlayers().get(0);
        int winnerRemainingTower = winner.getSchoolBoard().getTowerNumber();
        for (Player player: game.getPlayers()){
            if (player != game.getPlayers().get(0)){
                int remainingTower = player.getSchoolBoard().getTowerNumber();
                if (remainingTower < winnerRemainingTower){
                    winnerRemainingTower = remainingTower;
                    winner = player;
                }
                else if (remainingTower == winnerRemainingTower)
                    winner = null;
            }
        }
        if (winner == null){
            winner = game.getPlayers().get(0);
            int winnerProfessorsNumber = winner.getSchoolBoard().getProfessorTable().size();
            for (Player player: game.getPlayers()){
                if (player != game.getPlayers().get(0)){
                    int professorsNumber = player.getSchoolBoard().getProfessorTable().size();
                    if (professorsNumber > winnerProfessorsNumber){
                        winnerProfessorsNumber = professorsNumber;
                        winner = player;
                    }
                }
            }
        }
        win(winner);
    }

    /**
     * Notify the winner and stop the game
     * @param player the player who wins the game
     */
    private void win(Player player){
        setGameState(GameState.END);
        for (Player p: game.getPlayers()){
            VirtualView virtualView = virtualViewMap.get(p.getUsername());
            if (p.getUsername().equals(player.getUsername())){
                virtualView.win();
            } else {
                virtualView.loss();
            }
        }
        endGame();
    }

    /**
     * Reset game instance
     */
    private void endGame(){
        Game.resetInstance();
    }

    /**
     * Manage player access. If the player is new, add him to the player list. If it's the first, ask for the number
     * of players and the difficulty of the game
     * @param username the player's username
     * @param virtualView the player's virtualView
     */
    public void login(String username, VirtualView virtualView){
        if (virtualViewMap.isEmpty()){
            addVirtualView(username, virtualView);
            game.addPlayer(new Player(username));
            virtualView.getPlayersNumber();
            virtualView.getGameMode();
        }
        else if (virtualViewMap.size() < game.getChosenPlayersNumber()){
            addVirtualView(username, virtualView);
            game.addPlayer(new Player(username));
            if (game.getNumCurrentPlayers() == game.getChosenPlayersNumber())
                startGame();
        }
    }

    /**
     * Initialize the game and start a new round
     */
    private void startGame(){
        try {
            game.setupGame();
            currentPlayer = game.getPlayers().get(0);
            setGameState(GameState.STARTED);
            String json = gson.toJson(game, Game.class);
            for (VirtualView virtualView: virtualViewMap.values())
                virtualView.startGame(json);
            newRound();
        } catch (EmptyBagException | SchoolBoardException ignored){}
    }

    /**
     * Send an updated copy of the game model to all clients
     */
    private void sendGame(){
        String json = gson.toJson(game, Game.class);
        for (VirtualView virtualView: virtualViewMap.values())
            virtualView.gameModel(json);
    }

    /**
     * Check if the username is free
     * @param username the username to check
     * @return true if the username has not already been chosen by another player
     */
    public boolean checkUsername(String username) {
        return !game.checkUsername(username);
    }

    /**
     * Check if the lobby can accept another client or not
     * @return true if the next player to connect must be the last one
     */
    public boolean isLobbyFill(){
        return game.getPlayers().size() == game.getChosenPlayersNumber() - 1;
    }

    /**
     * Close the game and send a message to all player that the game is ended
     */
    public void disconnect(){
        setGameState(GameState.END);
        for (VirtualView virtualView: virtualViewMap.values()) {
            virtualView.printString("Un giocatore si è disconnesso, la partita verrà chiusa");
        }
        endGame();
    }

    private void addVirtualView(String username, VirtualView virtualView){
        virtualViewMap.put(username, virtualView);
    }

    public Map<String, VirtualView> getVirtualViewMap(){
        return this.virtualViewMap;
    }

    private void removeVirtualView(String username){
        VirtualView virtualView = virtualViewMap.remove(username);
        game.removePlayerByUsername(username);
    }
}
