package it.polimi.ingsw.network.messages;

public class PlayerNumber extends Message{
    private final int playerNumber;

    public PlayerNumber(String username, int playerNumber){
        super(username, Request.PLAYERNUMBER);
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }
}
