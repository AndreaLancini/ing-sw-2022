package it.polimi.ingsw.model.player;

import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.enumerations.TowerColor;
import it.polimi.ingsw.exceptions.player.SchoolBoardException;
import it.polimi.ingsw.model.map.Cloud;
import it.polimi.ingsw.model.map.Island;
import it.polimi.ingsw.model.pawns.Professor;
import it.polimi.ingsw.model.pawns.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The class represents a player's school board
 */
public class SchoolBoard implements Serializable {
    private final UUID schoolBoardID;
    private int towerNumber;
    private final TowerColor towerColor;
    private final int maxTowers;
    private final ArrayList<Student> entrance;
    private final int entranceSize;
    private final ArrayList<Student>[] diningRoom;
    private final ArrayList<Professor> professorTable;

    /**
     * Constructor of the class
     * @param towerColor color of the tower
     * @param maxTowers number of towers to place to win the game
     * @param entranceSize size of the entrance
     */
    public SchoolBoard(TowerColor towerColor, int maxTowers, int entranceSize){
        schoolBoardID = UUID.randomUUID();
        this.towerColor = towerColor;
        this.maxTowers = maxTowers;
        towerNumber = maxTowers;
        this.entranceSize = entranceSize;
        entrance = new ArrayList<>();
        diningRoom = new ArrayList[PawnColor.values().length];
        for(PawnColor color : PawnColor.values()){
            diningRoom[color.getValue()] = new ArrayList<>();
        }
        professorTable = new ArrayList<>();
    }

    public UUID getSchoolBoardID(){
        return this.schoolBoardID;
    }

    public int getTowerNumber(){
        return this.towerNumber;
    }

    public TowerColor getTowerColor(){
        return this.towerColor;
    }

    public ArrayList<Student> getEntrance(){
        return this.entrance;
    }

    public int getEntranceSize(){
        return this.entranceSize;
    }

    public ArrayList<Student>[] getDiningRoom(){return this.diningRoom;}

    public ArrayList<Professor> getProfessorTable(){
        return this.professorTable;
    }

    /**
     * Method to add towers to the SchoolBoard
     * @param tower number of towers to add
     * @throws SchoolBoardException if the number of the towers is greater than the max
     */
    public void addTower(int tower) throws SchoolBoardException{
        if(towerNumber + tower > maxTowers) {
            throw new SchoolBoardException();
        }
        towerNumber += tower;
    }

    /**
     * Method to remove towers to the SchoolBoard
     * @param tower number of towers to remove
     * @throws SchoolBoardException if every tower is placed
     */
    public void removeTower(int tower) throws SchoolBoardException{
        if(towerNumber <= tower) {
            throw new SchoolBoardException();
        }
        towerNumber -= tower;
    }

    /**
     * Method to add a Student to the entrance
     * @param student the student to add
     * @throws SchoolBoardException if the entrance is already full
     */
    public void addStudentToEntrance(Student student) throws SchoolBoardException{
        if(entrance.size() == entranceSize) {
            throw new SchoolBoardException();
        }
        entrance.add(student);
    }

    /**
     * Method to remove a student from the entrance
     * @param student the student to remove
     * @throws SchoolBoardException if the entrance is already empty
     */
    public void removeStudentFromEntrance(Student student) throws SchoolBoardException{
        if (entrance.size() == 0){
            throw new SchoolBoardException();
        }
        entrance.remove(student);
    }

    /**
     * Method to add a student to the dining room
     * @param student the student to add
     * @return if the player gained a coin or not
     * @throws SchoolBoardException if the table of the student's color is already full
     */
    public boolean addStudentToDining(Student student) throws SchoolBoardException{
        if(diningRoom[student.getPawnColor().getValue()].size() < 10){
            diningRoom[student.getPawnColor().getValue()].add(student);
            return diningRoom[student.getPawnColor().getValue()].size() % 3 == 0;
        }
        throw new SchoolBoardException(1);

    }

    /**
     * Method to remove a Student from the dining room
     * @param color the color of the student to remove
     * @return the removed student or null if the table of the selected color is already empty
     * @throws SchoolBoardException if the table of the selected color is already empty
     */
    public Student removeStudentFromDining(PawnColor color) throws SchoolBoardException{
        if(diningRoom[color.getValue()].size() == 0){
           throw new SchoolBoardException();
        }
        return diningRoom[color.getValue()].remove(0);
    }

    /**
     * Method to move a student from the entrance to the dining room
     * @param color the color of the student to move
     * @return true if the player can take the coin, false otherwise
     * @throws SchoolBoardException if the table is already full or there isn't a student of the selected color
     */
    public boolean moveStudentToDining(PawnColor color) throws SchoolBoardException {
        boolean coin;
        for(Student s: entrance){
            if(s.getPawnColor() == color){
                coin = addStudentToDining(s);
                removeStudentFromEntrance(s);
                return coin;
            }
        }
        throw new SchoolBoardException(2);
    }

    /**
     * Move a student from the entrance to the island
     * @param color the color of the student to move
     * @param island the island where the student is moved
     * @throws SchoolBoardException if there isn't a student of the selected color
     */
    public void moveStudentToIsland(PawnColor color, Island island) throws SchoolBoardException {
        for (Student s: entrance){
            if (s.getPawnColor() == color){
                island.addStudent(s);
                removeStudentFromEntrance(s);
                return;
            }
        }
        throw new SchoolBoardException();
    }

    /**
     * Count the number of students of selected color in the dining room
     * @param color the color to count
     * @return the number of students
     */
    public int checkStudentNumber(PawnColor color){
        int sum = 0;
        for (Student s: diningRoom[color.getValue()]){
            sum++;
        }
        return sum;
    }

    /**
     * Method to add a professor to the professors' table
     * @param professor the professor to add
     * @throws SchoolBoardException if the table already contains a professor of the selected color
     */
    public void addProfessor(Professor professor) throws SchoolBoardException{
        for(Professor p: professorTable){
            if(p.getPawnColor() == professor.getPawnColor())
                throw new SchoolBoardException();
        }
        professorTable.add(professor);
    }

    /**
     * Method to remove a professor from the professors' table
     * @param color the color of the professor to remove
     * @return the professor removed
     * @throws SchoolBoardException if the professor of the selected color is not in the table
     */
    public Professor removeProfessor(PawnColor color) throws SchoolBoardException{
        for(Professor p: professorTable){
            if(p.getPawnColor() == color){
                professorTable.remove(p);
                return p;
            }
        }
        throw new SchoolBoardException();
    }

    /**
     * Take all students from a cloud
     * @param cloud the cloud where take the students
     * @throws SchoolBoardException if the entrance is already full
     */
    public void takeStudentsFromCloud(Cloud cloud) throws SchoolBoardException {
        while (cloud.getStudents().size() > 0){
            Student student = cloud.getStudents().get(0);
            cloud.removeStudent(student);
            addStudentToEntrance(student);
        }
    }

}
