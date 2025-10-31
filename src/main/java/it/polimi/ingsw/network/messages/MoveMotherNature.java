package it.polimi.ingsw.network.messages;

public class MoveMotherNature extends Message{
    private final int pos;

    public MoveMotherNature(String username, int pos){
        super(username, Request.MOVE_MOTHER_NATURE);
        this.pos = pos;
    }

    public int getPos() {
        return this.pos;
    }
}
