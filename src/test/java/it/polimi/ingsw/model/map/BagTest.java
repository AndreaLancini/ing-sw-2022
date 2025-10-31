package it.polimi.ingsw.model.map;

import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.exceptions.map.EmptyBagException;
import it.polimi.ingsw.model.pawns.Student;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class BagTest {

    private static Bag bag;
    private static ArrayList<Student> students;

    @BeforeAll
    static void setUp() {
        bag = new Bag();
        students = new ArrayList<>();
    }

    @AfterAll
    static void tearDown() {
        bag = null;
        students = null;
    }



    @Test
    public void extractStudentTest() throws EmptyBagException {
        PawnColor c1 = PawnColor.BLUE;
        Student s1 = new Student(c1);

        bag.addStudent(s1);
        assertEquals(s1, bag.extractStudent());
        assertEquals(c1, s1.getPawnColor());
    }

    @Test
    public void checkFirstBagInitialization() throws EmptyBagException {
        int i = 0;
        int [] studentPerColor = new int[PawnColor.values().length];
        int [] arrayCheck = {2, 2, 2, 2, 2};

        bag.initFirstBag();
        assertEquals(10, bag.getStudents().size());

        while(i < 10) {
            Student s = bag.extractStudent();
            studentPerColor[s.getPawnColor().getValue()] += 1;
            i++;
        }
        assertArrayEquals(arrayCheck, studentPerColor);

       assertEquals(0, bag.getStudents().size());

    }

    @Test
    public void checkBagInitialization() throws EmptyBagException {
        int i = 0;
        int [] studentPerColor = new int[PawnColor.values().length];
        int [] arrayCheck = {24, 24, 24, 24, 24};

        bag.initBag();

        assertEquals(120, bag.getStudents().size());

        while(i < 120) {
            Student s = bag.extractStudent();
            studentPerColor[s.getPawnColor().getValue()] += 1;
            i++;
        }

        assertArrayEquals(arrayCheck, studentPerColor);


        assertEquals(0, bag.getStudents().size());
    }


}