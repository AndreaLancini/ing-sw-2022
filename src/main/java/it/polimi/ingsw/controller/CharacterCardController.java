package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.CharacterType;
import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.enumerations.TowerColor;
import it.polimi.ingsw.exceptions.WinException;
import it.polimi.ingsw.exceptions.player.SchoolBoardException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.map.Island;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.messages.PlayCharacterCard;
import it.polimi.ingsw.view.VirtualView;

/**
 * The class represent the logic of the characters cards
 */
public class CharacterCardController {
    private final Game game;
    private final GameController gameController;
    private boolean soldier = false;
    private boolean cook = false;
    private boolean centaur = false;
    private boolean wizard = false;
    private PawnColor color = null;

    /**
     * Create new character card controller
     * @param game the game instance
     */
    public CharacterCardController(Game game, GameController gameController) {
        this.game = game;
        this.gameController = gameController;
    }

    /**
     * Method that performs an action based on the received character type
     * @param message received
     * @throws WinException if a player has placed all the towers
     */
    public void onMessage(PlayCharacterCard message) throws WinException {
        if (game.getPanel().getCharactersDeck().containsKey(message.getCharacterType())){
            Player player = game.getPlayerByUsername(message.getUsername());
            int cost = game.getPanel().getCharactersDeck().get(message.getCharacterType());
            switch (message.getCharacterType()){
                case BISHOP:
                    if (player.getCoins() >= cost){
                        player.removeCoins(cost);
                        game.getPanel().addCoins(cost);
                        playBishop(player, game.getPanel().getIslands().get(message.getIsland()));
                        game.getPanel().getCharactersDeck().replace(CharacterType.BISHOP, cost+1);
                    }
                    break;
                case SOLDIER:
                    if (player.getCoins() >= cost){
                        player.removeCoins(cost);
                        game.getPanel().addCoins(cost);
                        soldier = true;
                        game.getPanel().getCharactersDeck().replace(CharacterType.SOLDIER, cost+1);
                    }
                    break;
                case COOK:
                    if (player.getCoins() >= cost){
                        player.removeCoins(cost);
                        game.getPanel().addCoins(cost);
                        cook = true;
                        game.getPanel().getCharactersDeck().replace(CharacterType.COOK, cost+1);
                    }
                    break;
                case MINSTREL:
                    if (player.getCoins() >= cost){
                        player.removeCoins(cost);
                        game.getPanel().addCoins(cost);
                        playMinstrel(player, message.getEntranceSwap(), message.getDiningSwap());
                        game.getPanel().getCharactersDeck().replace(CharacterType.MINSTREL, cost+1);
                    }
                    break;
                case CENTAUR:
                    if (player.getCoins() >= cost){
                        player.removeCoins(cost);
                        game.getPanel().addCoins(cost);
                        centaur = true;
                        game.getPanel().getCharactersDeck().replace(CharacterType.CENTAUR, cost+1);
                    }
                    break;
                case COURTIER:
                    if (player.getCoins() >= cost){
                        player.removeCoins(cost);
                        game.getPanel().addCoins(cost);
                        color = message.getColor();
                        game.getPanel().getCharactersDeck().replace(CharacterType.COURTIER, cost+1);
                    }
                    break;
                case WIZARD:
                    if (player.getCoins() >= cost){
                        player.removeCoins(cost);
                        game.getPanel().addCoins(cost);
                        wizard = true;
                        game.getPanel().getCharactersDeck().replace(CharacterType.WIZARD, cost+1);
                    }
                    break;
                case COLLECTOR:
                    if (player.getCoins() >= cost){
                        player.removeCoins(cost);
                        game.getPanel().addCoins(cost);
                        playCollector(message.getColor());
                        game.getPanel().getCharactersDeck().replace(CharacterType.COLLECTOR, cost+1);
                    }
                    break;
                default:
                    VirtualView virtualView = gameController.getVirtualViewMap().get(message.getUsername());
                    virtualView.printString("Carta personaggio non corretta!");
                    break;
            }
        } else {
            VirtualView virtualView = gameController.getVirtualViewMap().get(message.getUsername());
            virtualView.printString("Carta personaggio non giocabile!");
        }
    }

    /**
     * Play bishop card
     * @param player the player who played the card
     * @param island the target island
     * @throws WinException if the player has placed all the towers
     */
    private void playBishop(Player player, Island island) throws WinException {
        Player playerMaxInfluence = player;
        int maxInfluence = player.calculateInfluence(island, centaur, color);
        for (Player p: game.getPlayers()){
            if (soldier)
                maxInfluence += 2;
            if (p != player){
                int influence = p.calculateInfluence(island, centaur, color);
                if (influence > maxInfluence){
                    maxInfluence = influence;
                    playerMaxInfluence = p;
                } else if (influence == maxInfluence)
                    playerMaxInfluence = null;
            }
        }
        if (playerMaxInfluence != null){ //if a player has the influence on that island > of all others players
            if (!island.isTowerPresent()){ //if on the island there isn't any tower, add new one
                try {
                    island.setTowerPresent(true);
                    playerMaxInfluence.getSchoolBoard().removeTower(1);
                    island.setTowerColor(playerMaxInfluence.getSchoolBoard().getTowerColor());
                } catch (SchoolBoardException e){
                    throw new WinException(playerMaxInfluence);
                }
            }
            else { //if on the island there is already one or more towers, then replace them
                try {
                    TowerColor towerColor = island.getTowerColor();
                    for (Player p: game.getPlayers()){
                        if (p.getSchoolBoard().getTowerColor() == towerColor){
                            try {
                                p.getSchoolBoard().addTower(island.getIslandNumber());
                            } catch (SchoolBoardException ignored){}
                            break;
                        }
                    }
                    playerMaxInfluence.getSchoolBoard().removeTower(island.getIslandNumber());
                    island.setTowerColor(playerMaxInfluence.getSchoolBoard().getTowerColor());
                } catch (SchoolBoardException e){
                    throw new WinException(playerMaxInfluence);
                }
            }
            if (playerMaxInfluence.getSchoolBoard().getTowerNumber() == 0)
                throw new WinException(playerMaxInfluence);
        }
        game.getPanel().unifyIslands(island);
    }

    /**
     * Play collector card
     * @param color the chosen color
     */
    private void playCollector(PawnColor color){
        for (Player player: game.getPlayers()){
            if (player.getSchoolBoard().checkStudentNumber(color) > 2){
                for (int i = 0; i < 3; i++) {
                    try {
                        game.getPanel().getBag().addStudent(player.getSchoolBoard().removeStudentFromDining(color));
                    } catch (SchoolBoardException ignored){}
                }
            } else {
                for (PawnColor pawnColor: PawnColor.values()){
                    while (player.getSchoolBoard().checkStudentNumber(pawnColor) > 0){
                        try {
                            game.getPanel().getBag().addStudent(player.getSchoolBoard().removeStudentFromDining(pawnColor));
                        } catch (SchoolBoardException ignored){}
                    }
                }
            }
        }
    }

    /**
     * Play minstrel card
     * @param player the player who played the card
     * @param entranceSwap the color of students to move from entrance into the dining room
     * @param diningSwap the color of students to move from dining room into the entrance
     */
    private void playMinstrel(Player player, PawnColor[] entranceSwap, PawnColor[] diningSwap){
        if (entranceSwap.length == diningSwap.length && entranceSwap.length <= 2) {
            for (int i = 0; i < diningSwap.length; i++) {
                try {
                    player.getSchoolBoard().moveStudentToDining(entranceSwap[i]);
                    player.getSchoolBoard().addStudentToEntrance(player.getSchoolBoard().removeStudentFromDining(diningSwap[i]));
                } catch (SchoolBoardException e){
                    System.out.println("Errore nel giocare la carta personaggio minstrel");
                }
            }
        } else {
            VirtualView virtualView = gameController.getVirtualViewMap().get(player.getUsername());
            virtualView.printString("Dati non corretti!");
        }
    }

    public boolean isCentaurActive() {
        return centaur;
    }

    public boolean isCookActive() {
        return cook;
    }

    public boolean isSoldierActive() {
        return soldier;
    }

    public boolean isWizardActive() {
        return wizard;
    }

    public PawnColor getColor() {
        return color;
    }

    public void reset(){
        soldier = false;
        cook = false;
        centaur = false;
        wizard = false;
        color = null;
    }
}
