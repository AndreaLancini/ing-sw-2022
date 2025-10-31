package it.polimi.ingsw.enumerations;

public enum TowerColor {
    BLACK(0),
    WHITE(1),
    GRAY(2);
    private final int value;

    TowerColor(int value){
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
