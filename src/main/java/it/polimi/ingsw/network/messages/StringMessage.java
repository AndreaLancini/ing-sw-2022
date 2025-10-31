package it.polimi.ingsw.network.messages;

public class StringMessage extends Message{
    private final String message;

    public StringMessage(String message){
        super(Request.STRING);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
