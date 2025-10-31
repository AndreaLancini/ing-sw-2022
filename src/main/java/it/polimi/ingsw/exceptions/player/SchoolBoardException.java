package it.polimi.ingsw.exceptions.player;

public class SchoolBoardException extends Exception{
    private final int id;

    public SchoolBoardException(){
        super();
        this.id = 0;
    }

    public SchoolBoardException(int id){
        super();
        this.id = id;
    }
}
