package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.Request;
import it.polimi.ingsw.network.messages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class represents a client inside the server
 */
public class ClientHandler implements Runnable {
    private Socket client;
    private Server server;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    /**
     * Initializes a new handler
     * @param client the socket connection
     */
    public ClientHandler(Socket client, Server server) {
        this.client = client;
        this.server = server;
    }


    /**
     * Connects to the client
     */
    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println("Non è possibile stabilire una connessione con " + client.getInetAddress());
            return;
        }
        System.out.println("Connesso a  " + client.getInetAddress());
        try {
            this.sendMessage(new ServerMessage(Request.LOGIN, false));
            handleConnection();
        } catch (IOException e) {
            System.out.println("Client " + client.getInetAddress() + " connessione interrotta");
            server.onDisconnect();
            try {
                client.close();
            } catch (IOException ignored) {}
        }

        try {
            client.close();
        } catch (IOException ignored) {}
    }

    /**
     * A loop that receives messages from the client
     * @throws IOException If a communication error occurs
     */
    private void handleConnection() throws IOException {
        try {
            while (true) {
                Object next = input.readObject();
                Message message = (Message) next;
                if (message.getMessageType() == Request.LOGIN){
                    if (server.usernameIsValid(message.getUsername()))
                        server.login(message.getUsername(), this);
                    else
                        this.sendMessage(new ServerMessage(Request.LOGIN, true));
                } else {
                    server.onMessage(message);
                }
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            System.out.println("Stream del client non valido");
        }
    }

    /**
     * Sends a message to the client
     * @param message the message to be sent
     * @throws IOException If a communication error occurs
     */
    public void sendMessage(Message message) throws IOException{
        output.writeObject(message);
    }
}
