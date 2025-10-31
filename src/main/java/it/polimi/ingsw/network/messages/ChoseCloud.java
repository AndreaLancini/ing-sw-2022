package it.polimi.ingsw.network.messages;

public class ChoseCloud extends Message{
    private final int cloud;

    public ChoseCloud(String username, int cloud){
        super(username, Request.CHOOSE_CLOUD);
        this.cloud = cloud;
    }

    public int getCloud() {
        return this.cloud;
    }
}
