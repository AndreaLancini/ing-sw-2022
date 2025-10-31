package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.enumerations.PawnColor;

public class MoveStudentDining extends Message{
    private final PawnColor color;

    public MoveStudentDining(String username, PawnColor color){
        super(username, Request.MOVE_STUDENT_DINING);
        this.color = color;
    }

    public PawnColor getColor() {
        return this.color;
    }
}
