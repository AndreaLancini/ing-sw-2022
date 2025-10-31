package it.polimi.ingsw.network.messages;

import java.io.Serializable;

/**
 * Abstract message class which must be extended by each message type.
 * Both server and clients will communicate using this generic type of message.
 */
public abstract class Message implements Serializable {

    private String username;
    private final Request messageType;

    Message(Request messageType) {
        this.messageType = messageType;
    }

    Message(String username, Request messageType) {
        this.username = username;
        this.messageType = messageType;
    }

    public String getUsername() {
        return username;
    }

    public Request getMessageType() {
        return messageType;
    }

}
