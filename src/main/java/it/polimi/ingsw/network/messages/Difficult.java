package it.polimi.ingsw.network.messages;

public class Difficult extends Message{
    private final boolean expert;

    public Difficult(String username, boolean expert){
        super(username, Request.DIFFICULT);
        this.expert = expert;
    }

    public boolean isExpert() {
        return this.expert;
    }
}
