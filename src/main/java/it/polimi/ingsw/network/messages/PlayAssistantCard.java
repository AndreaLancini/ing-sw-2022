package it.polimi.ingsw.network.messages;

public class PlayAssistantCard extends Message{
    private final int value;

    public PlayAssistantCard(String username, int value){
        super(username, Request.PLAY_ASSISTANT_CARD);
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
