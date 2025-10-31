package it.polimi.ingsw.model.cards;


import it.polimi.ingsw.exceptions.cards.HandException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Class represents a deck of assistant cards
 */
public class Deck implements Serializable {
    private final UUID deckID;
    private final ArrayList<AssistantCard> hand;
    private final ArrayList<AssistantCard> discardPile;

    /**
     * Create a new empty deck
     */
    public Deck(){
        deckID = UUID.randomUUID();
        hand = new ArrayList<>();
        discardPile = new ArrayList<>();
    }

    public UUID getDeckID(){
        return this.deckID;
    }

    public ArrayList<AssistantCard> getHand(){
        return this.hand;
    }

    public ArrayList<AssistantCard> getDiscardPile(){
        return this.discardPile;
    }

    /**
     * Add assistant card to hand
     * @param card the assistant card to add
     */
    public void addCardToHand(AssistantCard card) {
        hand.add(card);
    }

    /**
     * Remove assistant card from hand
     * @param card the card to remove
     */
    public void removeCardFromHand(AssistantCard card) {
        hand.remove(card);
    }

    /**
     * Add assistant card to discard pile
     * @param card the assistant card to add
     */
    public void addCardToDiscardPile(AssistantCard card){
        discardPile.add(0, card);
    }

    /**
     * Play an assistant card from the hand and place it into the discard pile
     * @param value the value of the card to play
     * @throws HandException if the card is already played
     */
    public void playAssistantCard(int value) throws HandException {
        for (AssistantCard card: hand){
            if (card.getValue() == value){
                removeCardFromHand(card);
                addCardToDiscardPile(card);
                return;
            }
        }
        throw new HandException();
    }

}
