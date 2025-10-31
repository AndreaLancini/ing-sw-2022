package it.polimi.ingsw.model.map;


import it.polimi.ingsw.enumerations.CharacterType;
import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.exceptions.map.EmptyBagException;
import it.polimi.ingsw.exceptions.map.EmptyProfessorException;
import it.polimi.ingsw.model.pawns.Professor;

import java.io.Serializable;
import java.util.*;

/**
 * The class represents the game panel, where the clouds, bag, islands and professors are stored
 */
public class GamePanel implements Serializable {
    private final ArrayList<Cloud> clouds;
    private final Bag bag;
    private final ArrayList<Island> islands;
    private final ArrayList<Professor> professors;
    private Map<CharacterType, Integer> charactersDeck;
    private int coins = 0;

    /**
     * Create the game panel
     */
    public GamePanel(){
        clouds = new ArrayList<>();
        bag = new Bag();
        professors = new ArrayList<>();
        islands = new ArrayList<>();
    }

    public ArrayList<Cloud> getClouds(){
        return this.clouds;
    }

    public Bag getBag(){
        return this.bag;
    }

    public ArrayList<Island> getIslands(){
        return this.islands;
    }

    /**
     * Move mother nature
     * @param pos the number of movements
     * @return the island where mother nature is after the movements
     */
    public Island moveMotherNature(int pos){
        for (Island island: islands){
            if (island.isMotherNature()){
                island.setMotherNature(false);
                int index = Math.floorMod(islands.indexOf(island) + pos, islands.size());
                islands.get(index).setMotherNature(true);
                return islands.get(index);
            }
        }
        return null;
    }

    /**
     * Check if an island can unify with the precedent island
     * @param island the island to check
     * @return true if the island can unify, false otherwise
     */
    public boolean canUnifyPrecedent(Island island){
        int index = Math.floorMod(islands.indexOf(island) - 1, islands.size());
        return island.isTowerPresent() && islands.get(index).isTowerPresent() && island.getTowerColor() == islands.get(index).getTowerColor();
    }

    /**
     * Check if the island can unify with the next island
     * @param island the island to check
     * @return true if the island can unify, false otherwise
     */
    public boolean canUnifyNext(Island island){
        int index = Math.floorMod(islands.indexOf(island) + 1, islands.size());
        return island.isTowerPresent() && islands.get(index).isTowerPresent() && island.getTowerColor() == islands.get(index).getTowerColor();
    }

    /**
     * Unify the island with the precedent and/or next if possible
     * @param island the island to unify
     */
    public void unifyIslands(Island island){
        int index;
        if (canUnifyPrecedent(island)){
            index = Math.floorMod(islands.indexOf(island) - 1, islands.size());
            island.getStudents().addAll(islands.get(index).getStudents());
            island.setGroup(true);
            island.increaseIslandNumber();
            islands.remove(index);
        }
        if (canUnifyNext(island)){
            index = Math.floorMod(islands.indexOf(island) + 1, islands.size());
            island.getStudents().addAll(islands.get(index).getStudents());
            island.setGroup(true);
            island.increaseIslandNumber();
            islands.remove(index);
        }
    }

    public ArrayList<Professor> getProfessors(){
        return this.professors;
    }

    /**
     * Remove a professor from the center of the map
     * @param color the color of the professor to remove
     * @return the removed professor
     * @throws EmptyProfessorException if the list don't have selected color
     */
    public Professor removeProfessor(PawnColor color) throws EmptyProfessorException {
        for (Professor professor: professors){
            if (professor.getPawnColor() == color){
                professors.remove(professor);
                return professor;
            }
        }
        throw new EmptyProfessorException();
    }

    /**
     * Create the islands, set mother nature on a random island, draw students and place them on the islands
     * @throws EmptyBagException if the bag is empty
     */
    public void initIslands() throws EmptyBagException {
        for (int i = 0; i < 12; i++) {
            islands.add(new Island());
        }
        Random random = new Random();
        int index = random.nextInt(islands.size());
        islands.get(index).setMotherNature(true);
        bag.initFirstBag();
        for (int i = Math.floorMod(index+1, islands.size()); i != index; i = Math.floorMod(i+1, islands.size())) {
            if (i != Math.floorMod(index+6, islands.size())){
                islands.get(i).addStudent(bag.extractStudent());
            }
        }
    }

    /**
     * Initialize the clouds
     * @param number the number of clouds to add
     * @param size the student size for each cloud
     */
    public void initClouds(int number, int size){
        for (int i = 0; i < number; i++) {
            clouds.add(new Cloud(size));
        }
    }

    /**
     * Initialize the professors
     */
    public void initProfessors(){
        for (PawnColor color: PawnColor.values()){
            professors.add(new Professor(color));
        }
    }

    /**
     * Fill all clouds with students
     * @throws EmptyBagException if the bag is empty
     */
    public void fillClouds() throws EmptyBagException {
        for (Cloud cloud: clouds){
            while (cloud.getStudents().size() < cloud.getSize()){
                cloud.addStudent(bag.extractStudent());
            }
        }
    }

    public Map<CharacterType, Integer> getCharactersDeck() {
        return charactersDeck;
    }

    /**
     * Draws the 3 character cards
     */
    public void initCharactersDeck(){
        charactersDeck = new HashMap<>();
        int[] temp = new Random().ints(0, CharacterType.values().length).distinct().limit(3).toArray();
        for (int i : temp) {
            CharacterType type = CharacterType.values()[i];
            if (type == CharacterType.MINSTREL || type == CharacterType.WIZARD)
                charactersDeck.put(type, 1);
            else if (type == CharacterType.SOLDIER || type == CharacterType.COOK)
                charactersDeck.put(type, 2);
            else
                charactersDeck.put(type, 3);
        }
    }

    public int getCoins() {
        return this.coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int coins){
        this.coins += coins;
    }

    public void removeCoins(int coins){
        this.coins -= coins;
    }
}
