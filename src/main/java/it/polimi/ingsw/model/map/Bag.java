package it.polimi.ingsw.model.map;

import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.exceptions.map.EmptyBagException;
import it.polimi.ingsw.model.pawns.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class represents a bag
 */
public class Bag implements Serializable {
    private final ArrayList<Student> students;

    /**
     * Create a new empty bag
     */
    public Bag(){
        students = new ArrayList<>();
    }

    /**
     * Add a student into the bag
     * @param student the student to add
     */
    public void addStudent(Student student){
        students.add(student);
    }

    /**
     * Draw a random student
     * @return the extracted student
     * @throws EmptyBagException if the bag is empty
     */
    public Student extractStudent() throws EmptyBagException {
        if (students.size() == 0){
            throw new EmptyBagException();
        }
        Random random = new Random();
        return students.remove(random.nextInt(students.size()));
    }

    /**
     * Add two students for each colors into the bag to initializing islands
     */
    public void initFirstBag(){
        for (PawnColor color: PawnColor.values()){
            addStudent(new Student(color));
            addStudent(new Student(color));
        }
    }

    /**
     * Initialize the bag with the remaining 120 students
     */
    public void initBag(){
        for (PawnColor color: PawnColor.values()){
            for (int i = 0; i < 24; i++) {
                addStudent(new Student(color));
            }
        }
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
