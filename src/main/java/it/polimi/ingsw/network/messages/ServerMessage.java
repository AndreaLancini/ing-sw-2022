package it.polimi.ingsw.network.messages;

public class ServerMessage extends Message{

    private boolean firstAttempt;

    public ServerMessage(Request messageType) {
        super(messageType);
    }

    public ServerMessage(Request messageType, boolean firstAttempt) {
        super(messageType);
        this.firstAttempt = firstAttempt;
    }

    public boolean isFirstAttempt() {
        return firstAttempt;
    }
}
