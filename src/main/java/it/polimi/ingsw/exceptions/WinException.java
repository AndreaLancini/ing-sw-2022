package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.model.player.Player;

public class WinException extends Exception{
    private final Player player;

    public WinException(Player player) {
        super();
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
