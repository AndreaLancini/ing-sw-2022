package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exceptions.cards.HandException;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    Deck deck;
    Player player;

    @BeforeEach
    void setUp() {
        player = new Player("player");
        deck = player.getDeck();
    }

    @Test
    void addCardToHand() {
        assertEquals(0, deck.getHand().size());
        deck.addCardToHand(new AssistantCard(1, 1));
        assertEquals(1, deck.getHand().size());
        assertEquals(1, deck.getHand().get(0).getValue());
        assertEquals(1, deck.getHand().get(0).getMaxMovements());
        deck.addCardToHand(new AssistantCard(10, 5));
        assertEquals(2, deck.getHand().size());
        assertEquals(10, deck.getHand().get(1).getValue());
        assertEquals(5, deck.getHand().get(1).getMaxMovements());
    }

    @Test
    void removeCardFromHand() {
        player.initDeck();
        assert deck != null;
        assertEquals(10, deck.getHand().size());
        for (int i = 10; i > 0; i--) {
            deck.removeCardFromHand(deck.getHand().get(i-1));
            assertEquals(i-1, deck.getHand().size());
        }
    }

    @Test
    void addCardToDiscardPile() {
        assertEquals(0, deck.getDiscardPile().size());
        deck.addCardToDiscardPile(new AssistantCard(1, 1));
        assertEquals(1, deck.getDiscardPile().size());
        assertEquals(1, deck.getDiscardPile().get(0).getValue());
        assertEquals(1, deck.getDiscardPile().get(0).getMaxMovements());
        deck.addCardToDiscardPile(new AssistantCard(10, 5));
        assertEquals(2, deck.getDiscardPile().size());
        assertEquals(10, deck.getDiscardPile().get(0).getValue());
        assertEquals(5, deck.getDiscardPile().get(0).getMaxMovements());
    }

    @Test
    void playAssistantCard() throws HandException {
        player.initDeck();
        assert deck != null;
        assertEquals(10, deck.getHand().size());
        assertEquals(0, deck.getDiscardPile().size());
        for (int i = 1; i < 11; i++) {
            deck.playAssistantCard(i);
            assertEquals(10-i, deck.getHand().size());
            assertEquals(i, deck.getDiscardPile().size());
            assertEquals(i, deck.getDiscardPile().get(0).getValue());
            for (AssistantCard card: deck.getHand())
                assertNotEquals(card.getValue(), i);
            boolean contain = false;
            for (AssistantCard card: deck.getDiscardPile()){
                if (card.getValue() == i) {
                    contain = true;
                    break;
                }
            }
            assertTrue(contain);
        }
        assertThrows(HandException.class, () -> deck.playAssistantCard(1));
    }
}
