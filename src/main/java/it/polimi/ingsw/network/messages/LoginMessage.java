package it.polimi.ingsw.network.messages;

public class LoginMessage extends Message{

    public LoginMessage(String username) {
        super(username, Request.LOGIN);
    }
}
