package it.polimi.ingsw.model.map;

import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.enumerations.TowerColor;
import it.polimi.ingsw.model.pawns.Student;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {

    public static Island island;
    private static boolean motherNature;

    @BeforeAll
    static void setUp() {
        island = new Island();
        motherNature = false;
    }

    @AfterAll
    static void tearDown() {
        island = null;
        motherNature = false;
    }

    @Test
    public void setMotherNatureCheck() {
        island.setMotherNature(true);
        assertTrue(island.isMotherNature());
        island.setMotherNature(false);
        assertFalse(island.isMotherNature());
    }

    @Test
    public void setTowerPresentCheck() {
        island.setTowerPresent(true);
        assertTrue(island.isTowerPresent());
        island.setTowerPresent(false);
        assertFalse(island.isTowerPresent());
    }

    @Test
    public void setTowerColorCheck() {
        island.setTowerColor(TowerColor.BLACK);
        assertEquals(TowerColor.BLACK, island.getTowerColor());
    }

    @Test
    public void getStudentCheck() {
        Student s = new Student(PawnColor.BLUE);
        island.addStudent(s);

        assertEquals(s, island.getStudents().get(0));
    }

    @Test
    public void setGroupCheck() {
        island.setGroup(true);
        assertTrue(island.isGroup());
        island.setGroup(false);
        assertFalse(island.isGroup());
    }

    @Test
    public void getIslandNumberCheck() {
        island.increaseIslandNumber();
        assertEquals(2, island.getIslandNumber());
    }

}