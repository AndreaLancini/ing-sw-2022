package it.polimi.ingsw.model.player;

import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.enumerations.TowerColor;
import it.polimi.ingsw.exceptions.player.SchoolBoardException;
import it.polimi.ingsw.model.map.Island;
import it.polimi.ingsw.model.pawns.Professor;
import it.polimi.ingsw.model.pawns.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player;
    Island island;

    @BeforeEach
    void setUp() {
        player = new Player("Test");
        player.setSchoolBoard(TowerColor.WHITE, 8, 7);
    }

    @Test
    void calculateInfluence() throws SchoolBoardException {
        island = new Island();
        island.addStudent(new Student(PawnColor.RED));
        island.addStudent(new Student(PawnColor.GREEN));
        island.addStudent(new Student(PawnColor.RED));
        island.addStudent(new Student(PawnColor.YELLOW));

        assertEquals(0, player.calculateInfluence(island, false, null));

        player.getSchoolBoard().addProfessor(new Professor(PawnColor.RED));
        assertEquals(2, player.calculateInfluence(island, false, null));

        island.setTowerColor(TowerColor.WHITE);
        island.setTowerPresent(true);
        assertEquals(3, player.calculateInfluence(island, false, null));

        island.increaseIslandNumber();
        assertEquals(4, player.calculateInfluence(island, false, null));

        //expert game
        assertEquals(2, player.calculateInfluence(island, true, null));
        assertEquals(2, player.calculateInfluence(island, false, PawnColor.RED));
    }

    @Test
    void initDeck(){
        player.initDeck();
        assertEquals(0, player.getDeck().getDiscardPile().size());
        assertEquals(10, player.getDeck().getHand().size());
        assertEquals(1, player.getDeck().getHand().get(0).getValue());
        assertEquals(1, player.getDeck().getHand().get(0).getMaxMovements());
        assertEquals(2, player.getDeck().getHand().get(1).getValue());
        assertEquals(1, player.getDeck().getHand().get(1).getMaxMovements());
        assertEquals(3, player.getDeck().getHand().get(2).getValue());
        assertEquals(2, player.getDeck().getHand().get(2).getMaxMovements());
        assertEquals(4, player.getDeck().getHand().get(3).getValue());
        assertEquals(2, player.getDeck().getHand().get(3).getMaxMovements());
        assertEquals(5, player.getDeck().getHand().get(4).getValue());
        assertEquals(3, player.getDeck().getHand().get(4).getMaxMovements());
        assertEquals(6, player.getDeck().getHand().get(5).getValue());
        assertEquals(3, player.getDeck().getHand().get(5).getMaxMovements());
        assertEquals(7, player.getDeck().getHand().get(6).getValue());
        assertEquals(4, player.getDeck().getHand().get(6).getMaxMovements());
        assertEquals(8, player.getDeck().getHand().get(7).getValue());
        assertEquals(4, player.getDeck().getHand().get(7).getMaxMovements());
        assertEquals(9, player.getDeck().getHand().get(8).getValue());
        assertEquals(5, player.getDeck().getHand().get(8).getMaxMovements());
        assertEquals(10, player.getDeck().getHand().get(9).getValue());
        assertEquals(5, player.getDeck().getHand().get(9).getMaxMovements());
    }

}
