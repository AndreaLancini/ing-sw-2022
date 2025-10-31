package it.polimi.ingsw.view.CLI.Screens;

import it.polimi.ingsw.enumerations.CharacterType;
import it.polimi.ingsw.enumerations.PawnColor;
import it.polimi.ingsw.model.pawns.Student;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.messages.*;

import java.util.ArrayList;
import java.util.Map;


/**
 * Screen to show during the action phase
 */
public class ActionScreen extends Screen{

    private int turnPhase = 0;


    /**
     * Method to invoke when the player has play his turn.
     */
    public void placeStudent(){turnPhase = 1;}
    public void moveMotherNature(){turnPhase = 2;}
    public void chooseCloud(){turnPhase = 3;}

    /**
     * Method that show the planning phase screen.
     * It shows the board and the Assistant card played by others players this turn.
     * When this player has to play his turn the Screen asks him to do so
     */
    @Override
    public void run() {
        printBoard(owner.getGame());
        int pos;
        int islandsNumber;
        while(!shouldStopScreen()) {
            switch(turnPhase) {
                case 1:
                    if(owner.getGame().isExpert()) {
                        playCharacterCard();
                        printBoard(owner.getGame());
                    }
                    islandsNumber = owner.getGame().getPanel().getIslands().size();
                    printBoard(owner.getGame());
                    System.out.println("Scegli il colore dello studente da muovere (0-Giallo, 1-Blu, 2-Verde, 3-Rosso, 4-Rosa)");
                    int color = input.nextInt();
                    while(color < 0 || color > 4) {
                        System.out.println("Colore non valido, riprova (0-Giallo, 1-Blu, 2-Verde, 3-Rosso, 4-Rosa)");
                        color = input.nextInt();
                    }
                    System.out.println("Dove vuoi posizionare lo studente?(1-"+islandsNumber+"Isole, 0-Scuola)");
                    pos = input.nextInt();
                    while(pos < 0 || pos > islandsNumber) {
                        System.out.println("Posizione non valida, riprova(1-"+islandsNumber+"Isole, 0-Scuola)");
                        pos = input.nextInt();
                    }

                    if (pos == 0)
                        owner.getClientSocket().sendResponse(new MoveStudentDining(owner.getUsername(), PawnColor.values()[color]));
                    else
                        owner.getClientSocket().sendResponse(new MoveStudentIsland(owner.getUsername(), PawnColor.values()[color], pos-1));


                    turnPhase = 0;
                    break;
                case 2:
                    if(owner.getGame().isExpert()) {
                        playCharacterCard();
                        printBoard(owner.getGame());
                    }
                    printBoard(owner.getGame());
                    islandsNumber = owner.getGame().getPanel().getIslands().size();
                    System.out.println("Di quante posizioni muovere madre natura?");
                    pos = input.nextInt();
                    while(pos < 1 || pos > islandsNumber) {
                        System.out.println("Posizione non valida, riprova");
                        pos = input.nextInt();
                    }

                    owner.getClientSocket().sendResponse(new MoveMotherNature(owner.getUsername(), pos));
                    turnPhase = 0;
                    break;
                case 3:
                    printBoard(owner.getGame());
                    System.out.println("Quale nuvola scegli?");
                    pos = input.nextInt();
                    while((pos < 1) || (pos > owner.getGame().getPanel().getClouds().size())) {
                        System.out.println("Nuvola non valida, riprova");
                        pos = input.nextInt();
                    }
                    owner.getClientSocket().sendResponse(new ChoseCloud(owner.getUsername(), pos-1));
                    turnPhase = 0;
                    break;

            }
        }
    }

    /**
     * Method to ask a player if he wants to play a character card and to make him play the one he wants to play
     */
    public void playCharacterCard(){
        Map<CharacterType, Integer> charactersMap = owner.getGame().getPanel().getCharactersDeck();
        ArrayList<CharacterType> characters = new ArrayList<>(charactersMap.keySet());
        Player player = owner.getGame().getPlayerByUsername(owner.getUsername());

        boolean canPlay=false;
        for(int price : charactersMap.values())
            if (price <= player.getCoins()) {
                canPlay = true;
                break;
            }

        if(!canPlay)
            return;

        //chiedo di scegliere quale carta giocare
        System.out.println("Quale carta vuoi giocare? (1-"+CHARACTERS[characters.get(0).getValue()]+", 2-"+CHARACTERS[characters.get(1).getValue()]+", 3-"+CHARACTERS[characters.get(2).getValue()]+",4-Info, 0-Annulla)");
        int toPlay = input.nextInt();
        boolean stop = false;
        while(!stop){
            switch (toPlay){
                case 0:
                    return;
                case 1:
                case 2:
                case 3:
                    if(player.getCoins() >= charactersMap.get(characters.get(toPlay-1))){
                        stop = true;
                    }else{
                        System.out.println("Monete insufficienti per giocare la carta scelta.");
                    }
                    break;
                case 4:
                    for(CharacterType c : characters)
                        printDescription(c);
                    break;
                default:
                    System.out.println("Scelta non valida, riprova.");
                    break;
            }

            System.out.println("Quale carta vuoi giocare?(1-"+CHARACTERS[characters.get(0).getValue()]+", 2-"+CHARACTERS[characters.get(1).getValue()]+", 3-"+CHARACTERS[characters.get(2).getValue()]+",4-Info, 0-Annulla");
            toPlay = input.nextInt();
        }

        toPlay--;
        int color;
        switch (characters.get(toPlay)){

            case BISHOP:
                System.out.println("Scegli l'isola su cui calcolare l'influenza");
                int island = input.nextInt();
                while (island < 1 || island >owner.getGame().getPanel().getIslands().size()){
                    System.out.println("Isola non valida, Scegli l'isola su cui calcolare l'influenza");
                    island = input.nextInt();
                }
                owner.getClientSocket().sendResponse(new PlayCharacterCard(owner.getUsername(), characters.get(toPlay), island-1));
                break;
            case SOLDIER:
                soldier = true;
                owner.getClientSocket().sendResponse(new PlayCharacterCard(owner.getUsername(), characters.get(toPlay)));
                break;
            case COOK:
                cook = true;
                owner.getClientSocket().sendResponse(new PlayCharacterCard(owner.getUsername(), characters.get(toPlay)));
                break;
            case MINSTREL:
                int cont = 0;
                ArrayList<PawnColor> entrance = new ArrayList<>();
                ArrayList<PawnColor> diningRoom = new ArrayList<>();
                while (cont < 2){
                    int colorEntrance;
                    System.out.print("Inserisci il colore dello studente dell'entrata da scambiare:");
                    do{
                        if(entrance.size() > cont)
                            entrance.remove(cont);

                        System.out.println("(0-Giallo, 1-Blu, 2-Verde, 3-Rosso, 4-Rosa)");
                        colorEntrance = input.nextInt();
                        if(colorEntrance >= 0 && colorEntrance <= 4)
                            entrance.add(PawnColor.values()[colorEntrance]);
                        else
                            System.out.print("Colore non valido, riprova: ");
                    }while(colorEntrance < 0 || colorEntrance > 4 || containStudents(entrance, player.getSchoolBoard().getEntrance()));

                    int colorDining;
                    do{
                        if(diningRoom.size() > cont)
                            diningRoom.remove(cont);

                        System.out.println("(0-Giallo, 1-Blu, 2-Verde, 3-Rosso, 4-Rosa)");
                        colorDining = input.nextInt();
                        if(colorDining >= 0 && colorDining <= 4)
                            diningRoom.add(PawnColor.values()[colorDining]);
                        else
                            System.out.print("Colore non valido, riprova: ");
                    }while(colorDining < 0 || colorDining > 4 || containStudents(diningRoom, player.getSchoolBoard().getDiningRoom()));

                    cont++;
                }
                PawnColor[] entranceArray = new PawnColor[entrance.size()];
                PawnColor[] diningRoomArray = new PawnColor[entrance.size()];

                for(int i = 0; i < entrance.size(); i++){
                    entranceArray[i] = entrance.get(i);
                    diningRoomArray[i] = diningRoom.get(i);
                }

                owner.getClientSocket().sendResponse(new PlayCharacterCard(owner.getUsername(), characters.get(toPlay), entranceArray, diningRoomArray));
                break;
            case CENTAUR:
                centaur = true;
                owner.getClientSocket().sendResponse(new PlayCharacterCard(owner.getUsername(), characters.get(toPlay)));
                break;
            case COURTIER:
                System.out.println("Inserisci il colore degli studenti da rimuovere dal calcolo dell'influenza: (0-Giallo, 1-Blu, 2-Verde, 3-Rosso, 4-Rosa)");
                color = input.nextInt();
                while(color < 0 || color > 4) {
                    System.out.println("Colore non valido, riprova: (0-Giallo, 1-Blu, 2-Verde, 3-Rosso, 4-Rosa)");
                    color = input.nextInt();
                }
                owner.getClientSocket().sendResponse(new PlayCharacterCard(owner.getUsername(), characters.get(toPlay), PawnColor.values()[color]));
                break;
            case WIZARD:
                wizard =  true;
                owner.getClientSocket().sendResponse(new PlayCharacterCard(owner.getUsername(), characters.get(toPlay)));
                break;
            case COLLECTOR:
                System.out.println("Inserisci il colore degli studenti da rimuovere dalla sala da pranzo+: (0-Giallo, 1-Blu, 2-Verde, 3-Rosso, 4-Rosa)");
                color = input.nextInt();
                while(color < 0 || color > 4) {
                    System.out.println("Colore non valido, riprova: (0-Giallo, 1-Blu, 2-Verde, 3-Rosso, 4-Rosa)");
                    color = input.nextInt();
                }
                owner.getClientSocket().sendResponse(new PlayCharacterCard(owner.getUsername(), characters.get(toPlay)));
                break;
        }
    }

    /**
     * Method to print the descriptions of the character cards
     * @param character the character card to print
     */
    public void printDescription(CharacterType character){
        switch(character){
            case BISHOP:
                System.out.println("BISHOP: Scegli un'isola e calcola l'influenza come se madre natura avesse terminato il suo movimento lì. In questo turno madre natura si muoverà come di consueto e nell'isola dove terminerà il suo movimento la maggioranza verrà normalmente calcolata.");
                break;
            case SOLDIER:
                System.out.println("SOLDIER: In questo turno, durante il calcolo dell'influenza hai 2 punti influenza addizionali.");
                break;
            case COOK:
                System.out.println("COOK: Durante questo turno, prendi il controllo dei professori anche se nella tua sala hai lo stesso numero di studenti del giocatore che li controlla in quel momento.");
                break;
            case MINSTREL:
                System.out.println("MINSTREL: Puoi scambiare fra loro fino a due studenti presenti nella tua sala e nel tuo ingresso.");
                break;
            case CENTAUR:
                System.out.println("CENTAUR: Durante il calcolo dell'influenza su un'isola (o su un gruppo di isole), le torri presenti non vengono calcolate.");
                break;
            case COURTIER:
                System.out.println("COURTIER: Scegli un colore di studente; in questo turno, durante il calcolo dell'influenza quel colore non fornisce influenza.");
                break;
            case WIZARD:
                System.out.println("WIZARD: Puoi muovere madre natura fino a due isole addizionali rispetto a quanto indicato sulla carta assistente che hai giocato.");
                break;
            case COLLECTOR:
                System.out.println("COLLECTOR: Scegli un colore di studente; ogni giocatore (incluso te) deve rimettere nel sacchetto 3 studenti di quel colore presenti nella sua sala. Chi avesse meno di 3 studenti di quel colore, rimetterà tutti quelli che ha.");
                break;
        }
    }

    /**
     * method to check if the selected students are contained in the entrance
     * @param from the students to check
     * @param entrance the entrance of the schoolboard
     * @return true if all the students of the arraylist from are contained in the entrance one, false otherwise
     */
    public boolean containStudents(ArrayList<PawnColor> from, ArrayList<Student> entrance){

        int []countFrom = {0,0,0,0,0};
        int []countEntrance = {0,0,0,0,0};

        for(PawnColor c : from)
            countFrom[c.getValue()]++;

        for(Student s : entrance)
            countEntrance[s.getPawnColor().getValue()]++;

        for (int i=0; i<5; i++)
            if(countFrom[i] > countEntrance[i])
                return false;

        return true;
    }

    /**
     * method to check if the selected students are contained in the dining room
     * @param from the students to check
     * @param diningRoom the dining room of the schoolboard
     * @return true if all the students of the arraylist from are contained in the dining room one, false otherwise
     */
    public boolean containStudents(ArrayList<PawnColor> from, ArrayList<Student>[] diningRoom){

        int []countFrom = {0,0,0,0,0};

        for(PawnColor c : from)
            countFrom[c.getValue()]++;

        for (int i=0; i<5; i++)
            if(countFrom[i] > diningRoom[i].size())
                return false;

        return true;
    }
}
