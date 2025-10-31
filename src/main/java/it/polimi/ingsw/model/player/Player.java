package it.polimi.ingsw.model.player;


import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.enumerations.TowerColor;
import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.map.Island;
import it.polimi.ingsw.model.pawns.Professor;
import it.polimi.ingsw.model.pawns.Student;

import java.io.Serializable;
import java.util.UUID;

/**
 * The class represents a single player
 */
public class Player implements Serializable {
    private final UUID playerID;
    private final String username;
    private SchoolBoard schoolBoard;
    private final Deck deck;
    private int coins;

    /**
     * Create a new player
     * @param username the name of the player
     */
    public Player(String username){
        playerID = UUID.randomUUID();
        this.username = username;
        schoolBoard = null;
        deck = new Deck();
        coins = 1;
    }

    public UUID getPlayerID(){
        return this.playerID;
    }

    public String getUsername(){
        return this.username;
    }

    public SchoolBoard getSchoolBoard(){
        return this.schoolBoard;
    }

    public void setSchoolBoard(TowerColor color, int maxTower, int entranceSize){
        this.schoolBoard = new SchoolBoard(color, maxTower, entranceSize);
    }

    public Deck getDeck(){
        return this.deck;
    }

    /**
     * Calculate the player's influence
     * @param island the island where count the influence
     * @param skipTower a flag to skip the tower count
     * @return the player's influence
     */
    public int calculateInfluence(Island island, boolean skipTower, PawnColor color){
        int count = 0;
        for (Professor professor: schoolBoard.getProfessorTable()){
            if (color == null || professor.getPawnColor() != color) {
                for (Student student : island.getStudents()) {
                    if (student.getPawnColor() == professor.getPawnColor())
                        count++;
                }
            }
        }
        if (!skipTower && island.isTowerPresent() && island.getTowerColor() == schoolBoard.getTowerColor())
            count += island.getIslandNumber();
        return count;
    }

    /**
     * Initialize the player's deck
     */
    public void initDeck(){
        deck.addCardToHand(new AssistantCard(1,1));
        deck.addCardToHand(new AssistantCard(2,1));
        deck.addCardToHand(new AssistantCard(3,2));
        deck.addCardToHand(new AssistantCard(4,2));
        deck.addCardToHand(new AssistantCard(5,3));
        deck.addCardToHand(new AssistantCard(6,3));
        deck.addCardToHand(new AssistantCard(7,4));
        deck.addCardToHand(new AssistantCard(8,4));
        deck.addCardToHand(new AssistantCard(9,5));
        deck.addCardToHand(new AssistantCard(10,5));
    }

    public int getCoins(){
        return this.coins;
    }

    public void addCoins(int coins){
        this.coins += coins;
    }

    public void removeCoins(int coins){
        this.coins -= coins;
    }

}
