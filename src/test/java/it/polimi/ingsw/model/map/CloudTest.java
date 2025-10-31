package it.polimi.ingsw.model.map;

import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.exceptions.map.EmptyCloudException;
import it.polimi.ingsw.exceptions.map.MaxCloudException;
import it.polimi.ingsw.model.pawns.Student;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class CloudTest {

    private static Cloud cloud;
    private static int size;

    @BeforeAll
    static void setUp() {
        cloud = new Cloud(size);
        size = 3;
    }

    @AfterAll
    static void tearDown() {
        cloud = null;
        size = 0;
    }

    @Test
    public void addStudentCheck() {
        Student s = new Student(PawnColor.BLUE);

        cloud.addStudent(s);
        assertEquals(s, cloud.getStudents().get(0));
    }


    @Test
    public void removeStudentCheck() {
        Student s = new Student(PawnColor.GREEN);

        cloud.addStudent(s);
        cloud.removeStudent(s);

        assertEquals(0, cloud.getStudents().size());
    }

}