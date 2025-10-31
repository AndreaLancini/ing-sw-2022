package it.polimi.ingsw.network.messages;

public class StartGameMessage extends Message{
    private final String jsonModel;

    public StartGameMessage(String jsonModel) {
        super(Request.START_GAME);
        this.jsonModel = jsonModel;
    }

    public String getJsonModel() {
        return jsonModel;
    }
}
