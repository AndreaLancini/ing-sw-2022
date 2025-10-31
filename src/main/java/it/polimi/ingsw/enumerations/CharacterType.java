package it.polimi.ingsw.enumerations;

public enum CharacterType {
    BISHOP(0),
    SOLDIER(1),
    COOK(2),
    MINSTREL(3),
    CENTAUR(4),
    COURTIER(5),
    WIZARD(6),
    COLLECTOR(7);
    //JESTER(8),
    //SHAMAN(9),
    //PRINCESS(10),
    //FRIAR(11),

    private final int value;

    CharacterType(int value){
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
