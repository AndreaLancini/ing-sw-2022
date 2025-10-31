package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.enumerations.PawnColor;

public class MoveStudentIsland extends Message{
    private final PawnColor color;
    private final int island;


    public MoveStudentIsland(String username, PawnColor color, int island){
        super(username, Request.MOVE_STUDENT_ISLAND);
        this.color = color;
        this.island = island;
    }

    public int getIsland() {
        return this.island;
    }

    public PawnColor getColor() {
        return this.color;
    }
}
