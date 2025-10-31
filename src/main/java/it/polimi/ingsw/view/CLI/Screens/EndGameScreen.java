package it.polimi.ingsw.view.CLI.Screens;

/**
 * Screen to show when the game ends
 */
public class EndGameScreen extends Screen{

    private final boolean winner;

    public EndGameScreen(boolean winner) {
        this.winner = winner;
    }

    /**
     * Screen to print when the player win the game
     */
    public void printVictory(){
        System.out.println("VITTORIA!!");
    }

    /**
     * Screen to print when the player lose the game
     */
    public void printLoss(){
        System.out.println("SCONFITTA!!");
    }

    /**
     * Method to start the screen. It choose the correct Screen to print
     */
    @Override
    public void run() {
        if(winner){
            printVictory();
        }else{
            printLoss();
        }
        owner.stop();
    }
}
