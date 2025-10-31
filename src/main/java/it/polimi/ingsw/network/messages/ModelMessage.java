package it.polimi.ingsw.network.messages;

public class ModelMessage extends Message{

    private final String jsonModel;

    public ModelMessage(String jsonModel) {
        super(Request.MODEL);
        this.jsonModel = jsonModel;
    }

    public String getJsonModel() {
        return jsonModel;
    }
}
