# Prova Finale di Ingegneria del Software
## Gruppo PSP48 componenti: [Alessandro Finazzi], [Andrea Gallazzini], [Andrea Lancini]

## Funzionalità implementate
- Regole Complete
- CLI
- Socket

## Esecuzione
Richiede Java 11+
### Server
Per eseguire il Server digitare da terminale:
```
java -jar PSP48-server.jar
```
Verrà chiesto il numero di porta del Socket per il server
### Client
Per eseguire il Client (CLI) digitare da terminale:
```
java -jar PSP48-client.jar
```
Verranno chiesti i seguenti parametri:
- L'indirizzo IP del server
- La porta del Socket del server
- L'username da utilizzare all'interno del gioco
- (Solo per il primo giocatore che si connette) Il numero di giocatori totali della partita (2 o 3)
- (Solo per il primo giocatore che si connette) La difficoltà della partita (normale o esperto)
