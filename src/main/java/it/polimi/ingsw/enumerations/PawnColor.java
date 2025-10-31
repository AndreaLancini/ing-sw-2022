package it.polimi.ingsw.enumerations;

public enum PawnColor {
    YELLOW(0),
    BLUE(1),
    GREEN(2),
    RED(3),
    PINK(4);
    private final int value;

    PawnColor(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
