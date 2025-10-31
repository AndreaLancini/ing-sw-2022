package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.pawns.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Class represents a cloud
 */
public class Cloud implements Serializable {
    private final UUID cloudID;
    private final int size;
    private final ArrayList<Student> students;

    /**
     * Create a new empty cloud
     * @param size the size of the cloud
     */
    public Cloud(int size){
        cloudID = UUID.randomUUID();
        this.size = size;
        students = new ArrayList<>();
    }

    public UUID getCloudID() {
        return this.cloudID;
    }

    public int getSize() {
        return this.size;
    }

    public ArrayList<Student> getStudents() {
        return this.students;
    }

    /**
     * Add a student into the cloud
     * @param student the student to add
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Remove a student from the cloud
     * @param student the student to remove
     */
    public void removeStudent(Student student) {

        students.remove(student);
    }
}
