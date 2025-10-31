package it.polimi.ingsw.view.CLI.Screens;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.map.GamePanel;
import it.polimi.ingsw.network.messages.PlayAssistantCard;

import java.io.IOException;
import java.util.Scanner;

/**
 * Screen to show during the planning phase
 */
public class PlanningScreen extends Screen{
    private boolean yourTurn;
    private Deck yourDeck;



    /**
     * Method to invoke when the player has to choose his assistant card to play.
     */
    public void beginTurn(){
        yourTurn = true;
    }

    /**
     * Method that show the planning phase screen.
     * It shows the board and the Assistant card played by others players this turn.
     * When this player has to choose which assistant card to play the Screen asks him to do so
     */
    @Override
    public void run() {
        printBoardCard(owner.getGame());
        while(!shouldStopScreen()) {
            if(yourTurn) {
                printBoardCard(owner.getGame());

                System.out.println("Quale carta vuoi giocare? (Inserisci la velocità della carta)");
                int card = input.nextInt();
                while(card <1 || card>10){
                    System.out.println("Carta scelta non valida, riprova:");
                    card = input.nextInt();
                }
                owner.getClientSocket().sendResponse(new PlayAssistantCard(owner.getUsername(), card));
                yourTurn = false;
            }
        }

    }
}
