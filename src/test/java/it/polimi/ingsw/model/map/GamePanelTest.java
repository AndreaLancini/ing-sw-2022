package it.polimi.ingsw.model.map;

import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.enumerations.TowerColor;
import it.polimi.ingsw.exceptions.map.EmptyBagException;
import it.polimi.ingsw.model.pawns.Student;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class GamePanelTest {

    private static GamePanel panel;


    @BeforeEach
    void setUp() {
        panel = new GamePanel();

        for (int i = 0; i < 12; i++) {
            Island island = new Island();
            panel.getIslands().add(island);
        }
    }

    @AfterEach
    void tearDown() {
        panel = null;
    }

    @Test
    void moveMotherNature() {
        int pos = 4;
        int mn = 10;
        int index;

        panel.getIslands().get(mn).setMotherNature(true);
        Island i = panel.moveMotherNature(pos);
        assertNotNull(i);

        assertFalse(panel.getIslands().get(mn).isMotherNature());
        assertTrue(i.isMotherNature());

        index = (pos + mn) % panel.getIslands().size();

        assertEquals(index, panel.getIslands().indexOf(i));
    }

    @Test
    void canUnifyPrecedent() {
        int tower_pos = 5;

        panel.getIslands().get(tower_pos - 1).setTowerColor(TowerColor.BLACK);
        panel.getIslands().get(tower_pos).setTowerColor(TowerColor.BLACK);
        panel.getIslands().get(tower_pos).setTowerPresent(true);
        panel.getIslands().get(tower_pos - 1).setTowerPresent(true);
        assertTrue(panel.canUnifyPrecedent(panel.getIslands().get(tower_pos)));
        panel.getIslands().get(tower_pos - 1).setTowerPresent(false);
        assertFalse(panel.canUnifyPrecedent(panel.getIslands().get(tower_pos)));
    }

    @Test
    void canUnifyNext() {
        int tower_pos = 5;

        panel.getIslands().get(tower_pos + 1).setTowerColor(TowerColor.BLACK);
        panel.getIslands().get(tower_pos + 1).setTowerPresent(true);
        panel.getIslands().get(tower_pos).setTowerColor(TowerColor.BLACK);
        panel.getIslands().get(tower_pos).setTowerPresent(true);
        assertTrue(panel.canUnifyNext(panel.getIslands().get(tower_pos)));
        panel.getIslands().get(tower_pos + 1).setTowerPresent(false);
        assertFalse(panel.canUnifyNext(panel.getIslands().get(tower_pos)));
    }

    @Test
    void unifyIsland_Pre() {
        int index = 0;
        Island i = panel.getIslands().get(index);

        int old_size = panel.getIslands().size();

        panel.getIslands().get(panel.getIslands().indexOf(i)).setTowerPresent(true);
        panel.getIslands().get(Math.floorMod(panel.getIslands().indexOf(i) - 1, panel.getIslands().size())).setTowerPresent(true);
        panel.getIslands().get(panel.getIslands().indexOf(i)).setTowerColor(TowerColor.BLACK);
        panel.getIslands().get(Math.floorMod(panel.getIslands().indexOf(i) - 1, panel.getIslands().size())).setTowerColor(TowerColor.BLACK);
        panel.unifyIslands(panel.getIslands().get(panel.getIslands().indexOf(i)));
        assertEquals(old_size, panel.getIslands().size() + panel.getIslands().get(panel.getIslands().indexOf(i)).getIslandNumber() - 1);
        assertTrue(panel.getIslands().get(panel.getIslands().indexOf(i)).isGroup());
    }

    @Test
    void unifyIsland_Next() {
        int index = 11;
        Island i = panel.getIslands().get(index);

        int old_size = panel.getIslands().size();

        panel.getIslands().get(panel.getIslands().indexOf(i)).setTowerPresent(true);
        panel.getIslands().get(Math.floorMod(panel.getIslands().indexOf(i) + 1, panel.getIslands().size())).setTowerPresent(true);
        panel.getIslands().get(Math.floorMod(panel.getIslands().indexOf(i) + 1, panel.getIslands().size())).setTowerColor(TowerColor.BLACK);
        panel.getIslands().get(panel.getIslands().indexOf(i)).setTowerColor(TowerColor.BLACK);
        panel.unifyIslands(panel.getIslands().get(panel.getIslands().indexOf(i)));
        assertEquals(old_size, panel.getIslands().size() + panel.getIslands().get(panel.getIslands().indexOf(i)).getIslandNumber() - 1);
        assertTrue(panel.getIslands().get(panel.getIslands().indexOf(i)).isGroup());
    }

    @Test
    void unifyIsland_PreAndNext() {
        int index = 11;
        Island i = panel.getIslands().get(index);

        int old_size = panel.getIslands().size();

        panel.getIslands().get(panel.getIslands().indexOf(i)).setTowerPresent(true);
        panel.getIslands().get(Math.floorMod(panel.getIslands().indexOf(i) - 1, panel.getIslands().size())).setTowerPresent(true);
        panel.getIslands().get(Math.floorMod(panel.getIslands().indexOf(i) + 1, panel.getIslands().size())).setTowerPresent(true);
        panel.getIslands().get(Math.floorMod(panel.getIslands().indexOf(i) - 1, panel.getIslands().size())).setTowerColor(TowerColor.BLACK);
        panel.getIslands().get(Math.floorMod(panel.getIslands().indexOf(i) + 1, panel.getIslands().size())).setTowerColor(TowerColor.BLACK);
        panel.getIslands().get(panel.getIslands().indexOf(i)).setTowerColor(TowerColor.BLACK);
        panel.unifyIslands(panel.getIslands().get(panel.getIslands().indexOf(i)));
        assertEquals(old_size, panel.getIslands().size() + panel.getIslands().get(panel.getIslands().indexOf(i)).getIslandNumber() - 1);
        assertTrue(panel.getIslands().get(panel.getIslands().indexOf(i)).isGroup());
    }

    @Test
    void unifyIsland_Fail() {
        int index = 5;
        Island i = panel.getIslands().get(index);

        int old_size = panel.getIslands().size();

        panel.getIslands().get(panel.getIslands().indexOf(i)).setTowerPresent(true);
        panel.getIslands().get(Math.floorMod(panel.getIslands().indexOf(i) + 1, panel.getIslands().size())).setTowerPresent(false);
        panel.getIslands().get(Math.floorMod(panel.getIslands().indexOf(i) - 1, panel.getIslands().size())).setTowerPresent(true);
        panel.getIslands().get(panel.getIslands().indexOf(i)).setTowerColor(TowerColor.BLACK);
        panel.getIslands().get(Math.floorMod(panel.getIslands().indexOf(i) - 1, panel.getIslands().size())).setTowerColor(TowerColor.GRAY);
        panel.unifyIslands(panel.getIslands().get(panel.getIslands().indexOf(i)));
        assertEquals(old_size, panel.getIslands().size());
        assertFalse(panel.getIslands().get(panel.getIslands().indexOf(i)).isGroup());
    }

    @Test
    void checkStudentNumberAfterUnification() {
        int index = 5;
        Island i = panel.getIslands().get(index);
        Student s1 = new Student(PawnColor.BLUE);
        Student s2 = new Student(PawnColor.RED);

        for (int k = 0; k < 5; k++)
            panel.getIslands().get(panel.getIslands().indexOf(i)).addStudent(s1);
        for (int j = 0; j < 3; j++)
            panel.getIslands().get(panel.getIslands().indexOf(i)).addStudent(s2);
        for (int x = 0; x < 4; x++)
            panel.getIslands().get(panel.getIslands().indexOf(i) - 1).addStudent(s1);
        for (int y = 0; y < 2; y++)
            panel.getIslands().get(panel.getIslands().indexOf(i) - 1).addStudent(s2);

        panel.getIslands().get(panel.getIslands().indexOf(i)).setTowerPresent(true);
        panel.getIslands().get(panel.getIslands().indexOf(i) - 1).setTowerPresent(true);
        panel.getIslands().get(panel.getIslands().indexOf(i)).setTowerColor(TowerColor.BLACK);
        panel.getIslands().get(panel.getIslands().indexOf(i) - 1).setTowerColor(TowerColor.BLACK);
        panel.unifyIslands(panel.getIslands().get(panel.getIslands().indexOf(i)));

        assertEquals(14, panel.getIslands().get(panel.getIslands().indexOf(i)).getStudents().size());
    }

    @Test
    void initIslandTest() throws EmptyBagException {
        int index = 0;

        panel.getIslands().clear();

        assertEquals(0, panel.getIslands().size());

        panel.initIslands();

        assertEquals(12, panel.getIslands().size());

        for(Island i : panel.getIslands()) {
            if (i.isMotherNature())
                index = panel.getIslands().indexOf(i);
        }

        for(Island i : panel.getIslands()) {
            if(!i.isMotherNature() && panel.getIslands().indexOf(i) != Math.floorMod(index + 6, panel.getIslands().size()) )
                assertEquals(1, panel.getIslands().get(panel.getIslands().indexOf(i)).getStudents().size());
        }
    }

    @Test
    void initCharactersDeck(){
        panel.initCharactersDeck();
        assertEquals(3, panel.getCharactersDeck().size());
    }

}
