package it.polimi.ingsw.view.CLI.Screens;

import it.polimi.ingsw.enumerations.CharacterType;
import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.enumerations.TowerColor;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.map.Cloud;
import it.polimi.ingsw.model.map.GamePanel;
import it.polimi.ingsw.model.map.Island;
import it.polimi.ingsw.model.pawns.Professor;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.SchoolBoard;
import it.polimi.ingsw.view.CLI.CLI;


import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Abstact class that tell how a Scrren is made
 */
public abstract class Screen implements Runnable {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String GNOME = ANSI_YELLOW+"F"+ANSI_RESET;
    public static final String UNICORN = ANSI_BLUE+"U"+ANSI_RESET;
    public static final String FROG = ANSI_GREEN+"R"+ANSI_RESET;
    public static final String DRAGON = ANSI_RED+"D"+ANSI_RESET;
    public static final String FAIRY = ANSI_PURPLE+"F"+ANSI_RESET;

    public static final String[] STUDENTS = new String[]{GNOME, UNICORN, FROG, DRAGON, FAIRY};
    public static final String[] CHARACTERS = new String[]{" Bishop  ", " Soldier ", "  Cook   ", "Minstrel ", " Centaur ", "Courtier ", " Wizard  ", "Collector"};
    public static final String BACKSPACE = "\010";

    protected CLI owner;
    protected boolean stopScreen;
    protected Scanner input = new Scanner(System.in);
    protected boolean wizard = false;
    protected boolean centaur = false;
    protected boolean soldier = false;
    protected boolean cook = false;


    public void setOwner(CLI owner){
        this.owner = owner;
    }

    public CLI getOwner(){
        return owner;
    }

    /**
     * method to run the screen
     */
    abstract public void run();

    /**
     * @return a boolean that tells if the screen has to stop
     */
    synchronized protected boolean shouldStopScreen(){
        return stopScreen;
    }

    /**
     * Method to stop the screen
     */
    public synchronized void stopScreen(){
        stopScreen = true;
        notifyAll();
    }

    /**
     * Method to print the board state and the hand of the player
     * @param game the board state to print
     */
    public void printBoardCard(Game game){
        printBoard(game);
        System.out.println("Le tue carte:");
        for(Player p : game.getPlayers())
            if(p.getUsername().equals(owner.getUsername()))
                printHand(p.getDeck());


    }

    /**
     * Method to print the board state
     * @param game the board state to print
     */
    public void printBoard(Game game){

        for(Player p : game.getPlayers()){
            if(!p.getUsername().equals(owner.getUsername()))
                printSchoolBoard(p, game.isExpert());
        }

        if(wizard)
            System.out.print(CHARACTERS[6]+" attivo  ");
        if(centaur)
            System.out.print(CHARACTERS[4]+" attivo  ");
        if(soldier)
            System.out.print(CHARACTERS[1]+" attivo  ");
        if(cook)
            System.out.print(CHARACTERS[2]+" attivo  ");

        System.out.println("Isole:");
        printIslands(game.getPanel().getIslands());

        System.out.println("Nuvole:");
        printClouds(game.getPanel().getClouds());

        if(game.isExpert())
            printCharacterCards(game.getPanel().getCharactersDeck());

        for(Player p : game.getPlayers()){
            if(p.getUsername().equals(owner.getUsername()))
                printSchoolBoard(p, game.isExpert());
        }

    }

    /**
     * Method to print the hand of the player
     * @param deck the deck to print
     */
    public void printHand(Deck deck){
        for(AssistantCard c : deck.getHand())
            System.out.print("┌───────┐  ");
        System.out.println();
        for(AssistantCard c : deck.getHand())
            if(c.getValue() == 10)
                System.out.print("│  P:"+c.getValue()+" │  ");
            else
                System.out.print("│  V:"+c.getValue()+"  │  ");
        System.out.println();
        for(AssistantCard c : deck.getHand())
            System.out.print("│  P:"+c.getMaxMovements()+"  │  ");


        System.out.println();
        for(AssistantCard c : deck.getHand())
            System.out.print("└───────┘  ");
        System.out.println();
    }

    /**
     * Method to print a school board
     * @param player a player containing all the information to print the schoolboard
     * @param expert tells if the game is an expert game or not
     */
    public void printSchoolBoard(Player player, boolean expert){
        String p;
        int[] entrance = new int[5];
        int[] diningRoom  = new int[5];
        boolean[] professors = new boolean[5];
        String towerColor = "";
        SchoolBoard schoolBoard = player.getSchoolBoard();
        String username = player.getUsername();

        for(int n : entrance) {n = 0;}
        for(boolean b : professors) {b=false;}
        for(Student s:  schoolBoard.getEntrance())
            entrance[s.getPawnColor().getValue()]++;
        for(int n : diningRoom)
            n=0;
        for(int i=0; i<schoolBoard.getDiningRoom().length; i++)
            diningRoom[i] = schoolBoard.getDiningRoom()[i].size();
        for(Professor prof : schoolBoard.getProfessorTable())
            professors[prof.getPawnColor().getValue()] = true;
        switch (schoolBoard.getTowerColor().getValue()){
            case 0:
                towerColor = "Black";
                break;
            case 1:
                towerColor = "White";
                break;
            case 2:
                towerColor = "Gray ";
                break;
        }

        System.out.println("Player:" + username);
        if(expert) {
            System.out.println("┌──────────────┬───────────────────────┬───────┬───────┬───────┐");
            System.out.println("│   ENTRATA    │     SALA DA PRANZO    │ PROF. │ TORRI │ SOLDI │");
            System.out.println("├──────────────┼───────────────────────┼───────┼───────┼───────┤");

            if (professors[0])
                p = ANSI_YELLOW + "F" + ANSI_RESET;
            else
                p = " ";

            System.out.println("│  " + ANSI_YELLOW + "Folletti:" + entrance[0] + ANSI_RESET + "  │  " + getDiningRoom(0, diningRoom[0]) + " │   " + p + "   │   " + schoolBoard.getTowerNumber() + "   │   " + player.getCoins() + "   │");


            if (professors[1])
                p = ANSI_BLUE + "U" + ANSI_RESET;
            else
                p = " ";
            System.out.println("│  " + ANSI_BLUE + "Unicorni:" + entrance[1] + ANSI_RESET + "  │  " + getDiningRoom(1, diningRoom[1]) + " │   " + p + "   │ " + towerColor + " │       │");

            if (professors[2])
                p = ANSI_GREEN + "R" + ANSI_RESET;
            else
                p = " ";
            System.out.println("│  " + ANSI_GREEN + "Ranocchi:" + entrance[2] + ANSI_RESET + "  │  " + getDiningRoom(2, diningRoom[2]) + " │   " + p + "   │       │       │");

            if (professors[3])
                p = ANSI_RED + "D" + ANSI_RESET;
            else
                p = " ";
            System.out.println("│  " + ANSI_RED + "Draghi:" + entrance[3] + ANSI_RESET + "    │  " + getDiningRoom(3, diningRoom[3]) + " │   " + p + "   │       │       │");

            if (professors[4])
                p = ANSI_PURPLE + "F" + ANSI_RESET;
            else
                p = " ";
            System.out.println("│  " + ANSI_PURPLE + "Fate:" + entrance[4] + ANSI_RESET + "      │  " + getDiningRoom(4, diningRoom[4]) + " │   " + p + "   │       │       │");

            System.out.println("└──────────────┴───────────────────────┴───────┴───────┴───────┘");
        }else{
            System.out.println("┌──────────────┬───────────────────────┬───────┬───────┐");
            System.out.println("│   ENTRATA    │     SALA DA PRANZO    │ PROF. │ TORRI │");
            System.out.println("├──────────────┼───────────────────────┼───────┼───────┤");


            if (professors[0])
                p = ANSI_YELLOW + "F" + ANSI_RESET;
            else
                p = " ";

            System.out.println("│  " + ANSI_YELLOW + "Folletti:" + entrance[0] + ANSI_RESET + "  │  " + getDiningRoom(0, diningRoom[0]) + " │   " + p + "   │   " + schoolBoard.getTowerNumber() + "   │");


            if (professors[1])
                p = ANSI_BLUE + "U" + ANSI_RESET;
            else
                p = " ";
            System.out.println("│  " + ANSI_BLUE + "Unicorni:" + entrance[1] + ANSI_RESET + "  │  " + getDiningRoom(1, diningRoom[1]) + " │   " + p + "   │ " + towerColor + " │");

            if (professors[2])
                p = ANSI_GREEN + "R" + ANSI_RESET;
            else
                p = " ";
            System.out.println("│  " + ANSI_GREEN + "Ranocchi:" + entrance[2] + ANSI_RESET + "  │  " + getDiningRoom(2, diningRoom[2]) + " │   " + p + "   │       │");

            if (professors[3])
                p = ANSI_RED + "D" + ANSI_RESET;
            else
                p = " ";
            System.out.println("│  " + ANSI_RED + "Draghi:" + entrance[3] + ANSI_RESET + "    │  " + getDiningRoom(3, diningRoom[3]) + " │   " + p + "   │       │");

            if (professors[4])
                p = ANSI_PURPLE + "F" + ANSI_RESET;
            else
                p = " ";
            System.out.println("│  " + ANSI_PURPLE + "Fate:" + entrance[4] + ANSI_RESET + "      │  " + getDiningRoom(4, diningRoom[4]) + " │   " + p + "   │       │");

            System.out.println("└──────────────┴───────────────────────┴───────┴───────┘");
        }
    }

    /**
     * Method that returns the string to print into the dining room
     * @param color the color of the table to print
     * @param number the number of students of the table
     * @return the String to print
     */
    public String getDiningRoom(int color, int number){
        String s = "";
        String student = STUDENTS[color];
        for(int i=1; i<=10; i++){
            if(i<=number)
                s += student + " " ;
            else{
                if(i%3 == 0)
                    s += "0 " ;
                else
                    s += "  ";
            }

        }
        return s;
    }

    /**
     * Method to print the islands of the board
     * @param islands the ArrayList containing the islands to print
     */
    public void printIslands(ArrayList<Island> islands){
        int[][] students = new int[islands.size()][5];

        for(int i=0; i<islands.size(); i++){
            for(Student s : islands.get(i).getStudents()){
                students[i][s.getPawnColor().getValue()]++;
            }
        }

        //STAMPO LA PRIMA META
        for(int i=0; i<islands.size()/2; i++){
            System.out.print("┌─────────────┐  ");
        }
        System.out.println();
        for(int i=0; i<islands.size()/2; i++){
            System.out.print("│   Isola "+(i+1)+"   │  ");
        }
        System.out.println();
        for(int i=0; i<islands.size()/2; i++){
            System.out.print("│  "+ANSI_YELLOW+"F:"+students[i][0]+ANSI_RESET+"   "+ANSI_BLUE+"U:"+students[i][1]+ANSI_RESET+"  │  ");
        }
        System.out.println();
        for(int i=0; i<islands.size()/2; i++){
            System.out.print("│  "+ANSI_GREEN+"R:"+students[i][2]+ANSI_RESET+"   "+ANSI_RED+"D:"+students[i][3]+ANSI_RESET+"  │  ");
        }
        System.out.println();
        for(int i=0; i<islands.size()/2; i++){
            System.out.print("│     "+ANSI_PURPLE+"F:"+students[i][4]+ANSI_RESET+"     │  ");
        }
        System.out.println();
        for(int i=0; i<islands.size()/2; i++){
            System.out.print("│    Dim:"+islands.get(i).getIslandNumber()+"    │  ");
        }
        System.out.println();
        for(int i=0; i<islands.size()/2; i++){
            if(islands.get(i).isTowerPresent())
                System.out.print("│    Torri    │  ");
            else
                System.out.print("│             │  ");
        }
        System.out.println();
        for(int i=0; i<islands.size()/2; i++){
            if(islands.get(i).isTowerPresent()) {
                switch (islands.get(i).getTowerColor().getValue()){
                    case 0:
                        System.out.print("│   Bianche   │  ");
                        break;
                    case 1:
                        System.out.print("│    Nere     │  ");
                        break;
                    case 2:
                        System.out.print("│   Grigie    │  ");
                        break;
                }
            }
            else
                System.out.print("│             │  ");
        }
        System.out.println();
        for(int i=0; i<islands.size()/2; i++){
            if(islands.get(i).isMotherNature())
                System.out.print("│ MadreNatura │  ");
            else
                System.out.print("│             │  ");
        }
        System.out.println();
        for(int i=0; i<islands.size()/2; i++){
            System.out.print("└─────────────┘  ");
        }
        System.out.println();


        //STAMPO LA SECONDA META
        for(int i=islands.size()-1; i>=islands.size()/2; i--){
            System.out.print("┌─────────────┐  ");
        }
        System.out.println();
        for(int i=islands.size()-1; i>=islands.size()/2; i--){
            if(i>8)
                System.out.print("│   Isola "+(i+1)+"  │  ");
            else
                System.out.print("│   Isola "+(i+1)+"   │  ");
        }
        System.out.println();
        for(int i=islands.size()-1; i>=islands.size()/2; i--){
            System.out.print("│  "+ANSI_YELLOW+"F:"+students[i][0]+ANSI_RESET+"   "+ANSI_BLUE+"U:"+students[i][1]+ANSI_RESET+"  │  ");
        }
        System.out.println();
        for(int i=islands.size()-1; i>=islands.size()/2; i--){
            System.out.print("│  "+ANSI_GREEN+"R:"+students[i][2]+ANSI_RESET+"   "+ANSI_RED+"D:"+students[i][3]+ANSI_RESET+"  │  ");
        }
        System.out.println();
        for(int i=islands.size()-1; i>=islands.size()/2; i--){
            System.out.print("│     "+ANSI_PURPLE+"F:"+students[i][4]+ANSI_RESET+"     │  ");
        }
        System.out.println();
        for(int i=islands.size()-1; i>=islands.size()/2; i--){
            System.out.print("│    Dim:"+islands.get(i).getIslandNumber()+"    │  ");
        }
        System.out.println();
        for(int i=islands.size()-1; i>=islands.size()/2; i--){
            if(islands.get(i).isTowerPresent())
                System.out.print("│    Torri    │  ");
            else
                System.out.print("│             │  ");
        }
        System.out.println();
        for(int i=islands.size()-1; i>=islands.size()/2; i--){
            if(islands.get(i).isTowerPresent()) {
                switch (islands.get(i).getTowerColor().getValue()){
                    case 0:
                        System.out.print("│   Bianche   │  ");
                        break;
                    case 1:
                        System.out.print("│    Nere     │  ");
                        break;
                    case 2:
                        System.out.print("│   Grigie    │  ");
                        break;
                }
            }
            else
                System.out.print("│             │  ");
        }
        System.out.println();
        for(int i=islands.size()-1; i>=islands.size()/2; i--){
            if(islands.get(i).isMotherNature())
                System.out.print("│ MadreNatura │  ");
            else
                System.out.print("│             │  ");
        }
        System.out.println();
        for(int i=islands.size()-1; i>=islands.size()/2; i--){
            System.out.print("└─────────────┘  ");
        }
        System.out.println();
    }

    /**
     *  Method to print the clouds of the board
     * @param clouds the ArrayList containing the clouds to print
     */
    public void printClouds(ArrayList<Cloud> clouds){
        for(Cloud cloud : clouds)
            System.out.print("┌─────────┐  ");
        System.out.println();
        for(Cloud cloud : clouds)
            if (cloud.getStudents().size() == 0)
                System.out.print("│         │  ");
        else
            System.out.print("│  "+STUDENTS[cloud.getStudents().get(0).getPawnColor().getValue()]+"   "+STUDENTS[cloud.getStudents().get(1).getPawnColor().getValue()]+"  │  ");
        System.out.println();
        for(Cloud cloud : clouds)
            System.out.print("│         │  ");
        System.out.println();
        for(Cloud cloud : clouds)
            if (cloud.getStudents().size() == 0)
                System.out.print("│         │  ");
            else if(cloud.getStudents().size() == 4) System.out.print("│  "+STUDENTS[cloud.getStudents().get(2).getPawnColor().getValue()]+"   "+STUDENTS[cloud.getStudents().get(3).getPawnColor().getValue()]+"  │  ");
            else System.out.print("│    "+STUDENTS[cloud.getStudents().get(2).getPawnColor().getValue()]+"    │  ");
        System.out.println("   ");
        for(Cloud cloud : clouds)
            System.out.print("└─────────┘  ");
        System.out.println();
    }


    /**
     * Method to print characters' deck
     * @param characters a Map containing the character cards anc their prices
     */
    public void printCharacterCards(Map<CharacterType, Integer> characters){
        System.out.println("┌───────────┐  ┌───────────┐  ┌───────────┐");
        for(CharacterType c : characters.keySet())
            System.out.print("│ "+CHARACTERS[c.getValue()]+" │  ");
        System.out.println();
        for (int price : characters.values())
            System.out.print("│  Price:"+price+"  │  ");
        System.out.println();
        System.out.println("└───────────┘  └───────────┘  └───────────┘");
    }
}
