package it.polimi.ingsw.model.player;

import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.enumerations.TowerColor;
import it.polimi.ingsw.exceptions.map.EmptyCloudException;
import it.polimi.ingsw.exceptions.map.MaxCloudException;
import it.polimi.ingsw.exceptions.player.SchoolBoardException;
import it.polimi.ingsw.model.map.Cloud;
import it.polimi.ingsw.model.map.Island;
import it.polimi.ingsw.model.pawns.Professor;
import it.polimi.ingsw.model.pawns.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SchoolBoardTest {
    SchoolBoard board;
    Island island;
    Cloud cloud;

    @BeforeEach
    void setUp() {
        board = new SchoolBoard(TowerColor.WHITE, 8, 7);
    }

    @Test
    void addTower() throws SchoolBoardException {
        assertEquals(8, board.getTowerNumber());
        assertThrows(SchoolBoardException.class, () -> board.addTower(1));
        board.removeTower(4);
        board.addTower(2);
        assertEquals(6, board.getTowerNumber());
    }

    @Test
    void removeTower() throws SchoolBoardException {
        assertEquals(8, board.getTowerNumber());
        board.removeTower(5);
        assertEquals(3, board.getTowerNumber());
        assertThrows(SchoolBoardException.class, () -> board.removeTower(4));
    }

    @Test
    void addStudentToEntrance() throws SchoolBoardException {
        assertEquals(0, board.getEntrance().size());
        for (int i = 0; i < 7; i++) {
            board.addStudentToEntrance(new Student(PawnColor.BLUE));
            assertEquals(i+1, board.getEntrance().size());
        }
        assertThrows(SchoolBoardException.class, () -> board.addStudentToEntrance(new Student(PawnColor.GREEN)));
    }

    @Test
    void removeStudentFromEntrance() throws SchoolBoardException {
        Student student = new Student(PawnColor.YELLOW);
        board.addStudentToEntrance(student);
        assertEquals(1, board.getEntrance().size());
        board.removeStudentFromEntrance(student);
        assertEquals(0, board.getEntrance().size());
        assertThrows(SchoolBoardException.class, () -> board.removeStudentFromEntrance(student));
    }

    @Test
    void addStudentToDining() throws SchoolBoardException {
        for (int i = 0; i < 10; i++) {
            if (i+1 == 3 || i+1 == 6 || i+1 == 9)
                assertTrue(board.addStudentToDining(new Student(PawnColor.PINK)));
            else
                assertFalse(board.addStudentToDining(new Student(PawnColor.PINK)));
        }
        assertEquals(10, board.checkStudentNumber(PawnColor.PINK));
        assertThrows(SchoolBoardException.class, () -> board.addStudentToDining(new Student(PawnColor.PINK)));
    }

    @Test
    void removeStudentFromDining() throws SchoolBoardException {
        Student student = new Student(PawnColor.RED);
        board.addStudentToDining(student);
        assertEquals(1, board.checkStudentNumber(PawnColor.RED));
        assertEquals(student, board.removeStudentFromDining(PawnColor.RED));
        assertEquals(0, board.checkStudentNumber(PawnColor.RED));
        assertThrows(SchoolBoardException.class, () -> board.removeStudentFromDining(PawnColor.RED));
    }

    @Test
    void moveStudentToDining() throws SchoolBoardException {
        board.addStudentToEntrance(new Student(PawnColor.BLUE));
        board.addStudentToEntrance(new Student(PawnColor.BLUE));
        assertEquals(2, board.getEntrance().size());
        board.moveStudentToDining(PawnColor.BLUE);
        assertEquals(1, board.getEntrance().size());
        assertEquals(1, board.checkStudentNumber(PawnColor.BLUE));
        assertThrows(SchoolBoardException.class, () -> board.moveStudentToDining(PawnColor.YELLOW));
    }

    @Test
    void moveStudentToIsland() throws SchoolBoardException {
        island = new Island();
        assertEquals(0, island.getStudents().size());
        Student student = new Student(PawnColor.GREEN);
        board.addStudentToEntrance(student);
        board.moveStudentToIsland(PawnColor.GREEN, island);
        assertEquals(0, board.getEntrance().size());
        assertEquals(1, island.getStudents().size());
        assertEquals(student, island.getStudents().get(0));
        assertThrows(SchoolBoardException.class, () -> board.moveStudentToIsland(PawnColor.PINK, island));
    }

    @Test
    void checkStudentNumber() throws SchoolBoardException {
        for (PawnColor color: PawnColor.values()){
            assertEquals(0, board.checkStudentNumber(color));
            board.addStudentToEntrance(new Student(color));
            board.moveStudentToDining(color);
            assertEquals(1, board.checkStudentNumber(color));
        }

    }

    @Test
    void addProfessor() throws SchoolBoardException {
        assertEquals(0, board.getProfessorTable().size());
        for (PawnColor color: PawnColor.values()){
            board.addProfessor(new Professor(color));
            assertEquals(color, board.getProfessorTable().get(board.getProfessorTable().size()-1).getPawnColor());
        }
        assertEquals(PawnColor.values().length, board.getProfessorTable().size());
        assertThrows(SchoolBoardException.class, () -> board.addProfessor(new Professor(PawnColor.RED)));
    }

    @Test
    void removeProfessor() throws SchoolBoardException {
        Professor professor = new Professor(PawnColor.YELLOW);
        board.addProfessor(professor);
        assertEquals(professor, board.getProfessorTable().get(0));
        assertEquals(professor, board.removeProfessor(PawnColor.YELLOW));
        assertEquals(0, board.getProfessorTable().size());
        assertThrows(SchoolBoardException.class, () -> board.removeProfessor(PawnColor.BLUE));
    }

    @Test
    void takeStudentsFromCloud() throws MaxCloudException, SchoolBoardException, EmptyCloudException {
        cloud = new Cloud(3);
        while (cloud.getStudents().size() < cloud.getSize()){
            cloud.addStudent(new Student(PawnColor.GREEN));
        }
        assertEquals(cloud.getSize(), cloud.getStudents().size());
        board.takeStudentsFromCloud(cloud);
        assertEquals(0, cloud.getStudents().size());
        assertEquals(cloud.getSize(), board.getEntrance().size());
    }
}
