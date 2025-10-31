package it.polimi.ingsw.view.CLI.Screens;

/**
 * Screen to show while the client is waiting for messages from the server
 */
public class LoadingScreen extends Screen{

    /**
     * method to show the waiting screen
     */
    @Override
    public void run() {
        synchronized (this){
            int n = 0;
           System.out.print("Attendi il server");
            while(!shouldStopScreen()){
                n = (n+1) % 3;
                System.out.print(" .");
                try {
                    this.wait(500);
                } catch (InterruptedException e) {}

                if(n==0)
                    System.out.print(BACKSPACE+BACKSPACE+BACKSPACE+BACKSPACE+BACKSPACE+BACKSPACE);
            }
            System.out.println();
        }
    }
}

