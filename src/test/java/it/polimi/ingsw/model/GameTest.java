package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.enumerations.TowerColor;
import it.polimi.ingsw.exceptions.map.EmptyBagException;
import it.polimi.ingsw.exceptions.player.SchoolBoardException;
import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game;
    Player player1;
    Player player2;

    @BeforeEach
    void setUp() {
        game = Game.getInstance();
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.setExpert(false);
    }

    @AfterEach
    void tearDown() {
        Game.resetInstance();
    }

    @Test
    void getPlayerByUsername() {
        assertEquals(2, game.getPlayers().size());
        assertEquals(player1, game.getPlayerByUsername("Player1"));
        assertNull(game.getPlayerByUsername("Player5"));
    }

    @Test
    void addPlayer() {
        assertEquals(2, game.getPlayers().size());
        game.addPlayer(new Player("Player3"));
        assertEquals(3, game.getPlayers().size());
    }

    @Test
    void removePlayerByNickname() {
        assertEquals(2, game.getPlayers().size());
        assertTrue(game.removePlayerByUsername("Player2"));
        assertEquals(1, game.getPlayers().size());
        assertTrue(game.getPlayers().contains(player1));
        assertFalse(game.getPlayers().contains(player2));
    }

    @Test
    void setChosenMaxPlayers(){
        assertTrue(game.setChosenMaxPlayers(2));
        assertEquals(2, game.getChosenPlayersNumber());
        assertFalse(game.setChosenMaxPlayers(8));
        assertEquals(2, game.getChosenPlayersNumber());
    }

    @Test
    void checkUsername() {
        assertTrue(game.checkUsername("Player1"));
        assertFalse(game.checkUsername("Player3"));
    }

    @Test
    void setFirstPlayerRandom() {
        assertEquals(2, game.getPlayers().size());
        game.setFirstPlayerRandom();
        assertEquals(2, game.getPlayers().size());
        assertTrue(game.checkUsername("Player1"));
        assertTrue(game.checkUsername("Player2"));
    }

    @Test
    void sortPlayers() {
        game.getPlayers().get(game.getPlayers().indexOf(game.getPlayerByUsername("Player1"))).getDeck().addCardToDiscardPile(new AssistantCard(10, 5));
        game.getPlayers().get(game.getPlayers().indexOf(game.getPlayerByUsername("Player2"))).getDeck().addCardToDiscardPile(new AssistantCard(1, 1));
        game.sortPlayers();
        assertEquals(player2, game.getPlayers().get(0));
        assertEquals(player1, game.getPlayers().get(1));
        game.getPlayers().get(game.getPlayers().indexOf(game.getPlayerByUsername("Player1"))).getDeck().addCardToDiscardPile(new AssistantCard(5, 3));
        game.getPlayers().get(game.getPlayers().indexOf(game.getPlayerByUsername("Player2"))).getDeck().addCardToDiscardPile(new AssistantCard(5, 3));
        game.sortPlayers();
        assertEquals(player2, game.getPlayers().get(0));
        assertEquals(player1, game.getPlayers().get(1));
    }

    @Test
    void initSchoolBoardFor2Players() throws SchoolBoardException, EmptyBagException {
        game.setChosenMaxPlayers(2);
        game.getPanel().getBag().initBag();
        game.initSchoolBoard();
        assertEquals(TowerColor.values()[0], game.getPlayers().get(0).getSchoolBoard().getTowerColor());
        assertEquals(TowerColor.values()[1], game.getPlayers().get(1).getSchoolBoard().getTowerColor());
        assertEquals(7, game.getPlayers().get(0).getSchoolBoard().getEntrance().size());
        assertEquals(7, game.getPlayers().get(1).getSchoolBoard().getEntrance().size());
    }

    @Test
    void initSchoolBoardFor3Players() throws SchoolBoardException, EmptyBagException {
        game.setChosenMaxPlayers(3);
        game.getPanel().getBag().initBag();
        game.initSchoolBoard();
        assertEquals(TowerColor.values()[0], game.getPlayers().get(0).getSchoolBoard().getTowerColor());
        assertEquals(TowerColor.values()[1], game.getPlayers().get(1).getSchoolBoard().getTowerColor());
        assertEquals(9, game.getPlayers().get(0).getSchoolBoard().getEntrance().size());
        assertEquals(9, game.getPlayers().get(1).getSchoolBoard().getEntrance().size());
    }

    @Test
    void setupGameFor2Players() throws SchoolBoardException, EmptyBagException {
        game.setChosenMaxPlayers(2);
        game.setupGame();
        assertEquals(12, game.getPanel().getIslands().size());
        assertEquals(2, game.getPanel().getClouds().size());
        assertEquals(3, game.getPanel().getClouds().get(0).getSize());
        assertEquals(3, game.getPanel().getClouds().get(1).getSize());
        assertEquals(PawnColor.values().length, game.getPanel().getProfessors().size());
        for (Player player: game.getPlayers()){
            assertEquals(10, player.getDeck().getHand().size());
            assertEquals(0, player.getDeck().getDiscardPile().size());
        }
    }

    @Test
    void setupGameFor3Players() throws SchoolBoardException, EmptyBagException {
        game.setChosenMaxPlayers(3);
        game.setupGame();
        assertEquals(12, game.getPanel().getIslands().size());
        assertEquals(3, game.getPanel().getClouds().size());
        assertEquals(4, game.getPanel().getClouds().get(0).getSize());
        assertEquals(4, game.getPanel().getClouds().get(1).getSize());
        assertEquals(PawnColor.values().length, game.getPanel().getProfessors().size());
        for (Player player: game.getPlayers()){
            assertEquals(10, player.getDeck().getHand().size());
            assertEquals(0, player.getDeck().getDiscardPile().size());
        }
    }
}
