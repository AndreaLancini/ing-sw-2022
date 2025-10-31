package it.polimi.ingsw.model.map;

import it.polimi.ingsw.enumerations.TowerColor;
import it.polimi.ingsw.model.pawns.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The class represents a single island
 */
public class Island implements Serializable {
    private final UUID islandID;
    private boolean motherNature;
    private boolean towerPresent;
    private TowerColor towerColor;
    private final ArrayList<Student> students;
    private boolean group;
    private int islandNumber;

    /**
     * Create a new island
     */
    public Island(){
        islandID = UUID.randomUUID();
        motherNature = false;
        towerPresent = false;
        towerColor = null;
        students = new ArrayList<>();
        group = false;
        islandNumber = 1;
    }

    public UUID getIslandID() {
        return this.islandID;
    }

    public boolean isMotherNature() {
        return this.motherNature;
    }

    public void setMotherNature(boolean motherNature){
        this.motherNature = motherNature;
    }

    public boolean isTowerPresent() {
        return this.towerPresent;
    }

    public void setTowerPresent(boolean towerPresent) {
        this.towerPresent = towerPresent;
    }

    public TowerColor getTowerColor() {
        return this.towerColor;
    }

    public void setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
    }

    public ArrayList<Student> getStudents() {
        return this.students;
    }

    /**
     * Add a student in the island
     * @param student the student to add
     */
    public void addStudent(Student student){
        students.add(student);
    }

    public boolean isGroup() {
        return this.group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public int getIslandNumber(){
        return this.islandNumber;
    }

    /**
     * Increase the number of island in a group by 1
     */
    public void increaseIslandNumber(){
        this.islandNumber++;
    }
}
