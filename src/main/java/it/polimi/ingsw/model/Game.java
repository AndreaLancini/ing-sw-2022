package it.polimi.ingsw.model;


import it.polimi.ingsw.enumerations.TowerColor;
import it.polimi.ingsw.exceptions.map.EmptyBagException;
import it.polimi.ingsw.exceptions.player.SchoolBoardException;
import it.polimi.ingsw.model.map.GamePanel;
import it.polimi.ingsw.model.player.Player;


import java.io.Serializable;
import java.util.*;


/**
 * This Class represents the whole Game.
 */
public class Game implements Serializable {

    private static Game instance;

    public static final int MAX_PLAYERS = 3;

    private final GamePanel panel;
    private ArrayList<Player> players;
    private int chosenPlayersNumber = -1;

    boolean expert;

    /**
     * Default constructor.
     */
    public Game() {
        this.panel = new GamePanel();
        this.players = new ArrayList<>();

    }

    /**
     * @return the singleton instance.
     */
    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    public GamePanel getPanel(){
        return this.panel;
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    /**
     * Returns a player by his username
     * @param username of the player to be found
     * @return the player
     */
    public Player getPlayerByUsername(String username) {
        return players.stream()
                .filter(player -> username.equals(player.getUsername()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a player to the game
     * @param player to add
     */
    public void addPlayer(Player player) {
        players.add(player);

    }

    /**
     * Removes a player from the game
     * @param username of the player to remove
     * @return true if the player is correctly removed
     */
    public boolean removePlayerByUsername
    (String username) {
        return players.remove(getPlayerByUsername(username));
    }

    /**
     * Number of current players added in the game
     * @return the number of players.
     */
    public int getNumCurrentPlayers() {
        return players.size();
    }

    /**
     * Sets the max number of players chosen by the first player joining the game.
     * @param chosenMaxPlayers the max players number
     * @return true if the argument value is 0 < x < MAX_PLAYERS
     */
    public boolean setChosenMaxPlayers(int chosenMaxPlayers) {
        if (chosenMaxPlayers > 0 && chosenMaxPlayers <= MAX_PLAYERS) {
            this.chosenPlayersNumber = chosenMaxPlayers;
            return true;
        }
        return false;
    }

    /**
     * Give the number of players chosen by the first player
     * @return the number of players
     */
    public int getChosenPlayersNumber() {
        return chosenPlayersNumber;
    }


    /**
     * Checks if username is already used from a player
     * @param username of the player
     * @return true if the nickname is found, false otherwise
     */
    public boolean checkUsername(String username) {
        return players.stream()
                .anyMatch(p -> username.equals(p.getUsername()));
    }

    /**
     * Choose randomly the first player and order the players list
     */
    public void setFirstPlayerRandom() {
        Random random = new Random();
        int index = random.nextInt(players.size());
        ArrayList<Player> playersOrder = new ArrayList<>();
        for (int i = index; i < players.size(); i++) {
            playersOrder.add(players.get(i));
        }
        for (int i = 0; i < index; i++) {
            playersOrder.add(players.get(i));
        }
        players = playersOrder;
    }

    /**
     * Sort the players list from the one with the lowest assistant cart value to the highest
     */
    public void sortPlayers(){
        players.sort(Comparator.comparingInt(player -> player.getDeck().getDiscardPile().get(0).getValue()));
    }

    public boolean isExpert(){
        return this.expert;
    }

    /**
     * Sets the expert flag true if first player chose expert mode
     * @param expert if true is expert game, otherwise is normal game
     */
    public void setExpert(boolean expert) {
        this.expert = expert;
    }

    /**
     * Resets the game instance. After this operation, all the game data is lost
     */
    public static void resetInstance() {
        Game.instance = null;
    }

    /**
     * Initialize all players' school board and extract students from bag until entrance is full
     * @throws EmptyBagException if the bag is empty
     * @throws SchoolBoardException if the school board's entrance is already full
     */
    public void initSchoolBoard() throws EmptyBagException, SchoolBoardException {
        int i=0;
        for (Player player: players){
            if (chosenPlayersNumber == 2){
                player.setSchoolBoard(TowerColor.values()[i], 8, 7);
            }
            else if (chosenPlayersNumber == 3){
                player.setSchoolBoard(TowerColor.values()[i], 6, 9);
            }
            while (player.getSchoolBoard().getEntrance().size() < player.getSchoolBoard().getEntranceSize()){
                player.getSchoolBoard().addStudentToEntrance(panel.getBag().extractStudent());
            }
            i++;
        }
    }

    /**
     * Set up the game for 2 o 3 players
     * @throws EmptyBagException if the bag is empty
     * @throws SchoolBoardException if the school board's entrance is full
     */
    public void setupGame() throws EmptyBagException, SchoolBoardException {
        panel.initIslands();
        panel.getBag().initBag();
        if (chosenPlayersNumber == 2){
            panel.initClouds(2, 3);
        }
        if (chosenPlayersNumber == 3){
            panel.initClouds(3, 4);
        }
        panel.initProfessors();
        initSchoolBoard();
        for (Player player: players){
            player.initDeck();
        }
        if (isExpert()){
            panel.initCharactersDeck();
            panel.setCoins(20-getPlayers().size());
        }
        setFirstPlayerRandom();
    }

}
