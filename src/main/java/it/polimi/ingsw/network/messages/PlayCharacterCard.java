package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.enumerations.CharacterType;
import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.model.map.Island;

public class PlayCharacterCard extends Message{
    private final CharacterType characterType;
    private int island;
    private PawnColor color = null;
    private PawnColor[] entranceSwap = null;
    private PawnColor[] diningSwap = null;

    public PlayCharacterCard(String username, CharacterType characterType) {
        super(username, Request.PLAY_CHARACTER_CARD);
        this.characterType = characterType;
    }

    public PlayCharacterCard(String username, CharacterType characterType, int island){
        super(username, Request.PLAY_CHARACTER_CARD);
        this.characterType = characterType;
        this.island = island;
    }

    public PlayCharacterCard(String username, CharacterType characterType, PawnColor color){
        super(username, Request.PLAY_CHARACTER_CARD);
        this.characterType = characterType;
        this.color = color;
    }

    public PlayCharacterCard(String username, CharacterType characterType, PawnColor[] entranceSwap, PawnColor[] diningSwap){
        super(username, Request.PLAY_CHARACTER_CARD);
        this.characterType = characterType;
        this.entranceSwap = entranceSwap;
        this.diningSwap = diningSwap;
    }

    public CharacterType getCharacterType() {
        return this.characterType;
    }

    public PawnColor getColor() {
        return this.color;
    }

    public int getIsland(){
        return this.island;
    }

    public PawnColor[] getEntranceSwap() {
        return this.entranceSwap;
    }

    public PawnColor[] getDiningSwap() {
        return this.diningSwap;
    }
}
