package it.polimi.ingsw.model.cards;


import java.io.Serializable;
import java.util.UUID;

/**
 * Class represents a single assistant card
 */
public class AssistantCard implements Serializable {
    private final UUID cardID;
    private final int value;
    private final int maxMovements;

    /**
     * Create a new assistant card
     * @param value the value of the card used for the turn order
     * @param maxMovements the number of movements Mother Nature may perform
     */
    public AssistantCard(int value, int maxMovements){
        cardID = UUID.randomUUID();
        this.value = value;
        this.maxMovements = maxMovements;
    }

    public UUID getCardID(){
        return this.cardID;
    }

    public int getValue(){
        return this.value;
    }

    public int getMaxMovements() {
        return this.maxMovements;
    }
}
