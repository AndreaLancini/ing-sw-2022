package it.polimi.ingsw.model.pawns;

import it.polimi.ingsw.enumerations.PawnColor;

import java.io.Serializable;
import java.util.UUID;

public class Pawn implements Serializable {
    private final UUID pawnID;
    private final PawnColor pawnColor;

    public Pawn(PawnColor pawnColor){
        pawnID = UUID.randomUUID();
        this.pawnColor = pawnColor;
    }

    public UUID getPawnID(){
        return this.pawnID;
    }

    public PawnColor getPawnColor(){
        return this.pawnColor;
    }
}
