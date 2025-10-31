package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represent a socket from a client to fix a connection to a specified server
 */
public class ClientSocket implements Runnable {
    private Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Client owner;
    private AtomicBoolean shouldStop = new AtomicBoolean(false);

    /**
     * Initialize the client socket
     * @param server the socket connection
     * @param owner the client owner
     */
    ClientSocket(Socket server, Client owner) {
        this.server = server;
        this.owner = owner;
    }


    /**
     * Connects to the server
     */
    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(server.getOutputStream());
            input = new ObjectInputStream(server.getInputStream());
        } catch (IOException e) {
            System.out.println("Non è possibile aprire una connessione con " + server.getInetAddress());
            return;
        }

        try {
            handleMessage();
        } catch (IOException e) {
            System.out.println("Server " + server.getInetAddress() + " connessione interrotta");
        }

        try {
            server.close();
        } catch (IOException ignored) {}
    }

    /**
     * Sends a message to the server
     * @param message the message to be sent
     */
    public void sendResponse(Message message) {
        try {
            output.writeObject(message);
        } catch (IOException e) {
            System.out.println("Errore di comunicazione");
        }
    }

    /**
     * A loop that receives messages from the server
     * @throws IOException if a communication error occurs
     */
    public void handleMessage() throws IOException {
        try {
            boolean stop = false;
            while (!stop) {
                try {
                    Object next = input.readObject();
                    Message message = (Message) next;
                    owner.onMessage(message);
                } catch (IOException e) {
                    if (shouldStop.get()) {
                        stop = true;
                    } else {
                        throw e;
                    }
                }
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            System.out.println("Stream non valido per il server");
        }
    }

    public Client getClient()
    {
        return owner;
    }

    public void stop()
    {
        shouldStop.set(true);
        try {
            server.shutdownInput();
        } catch (IOException ignored) {}
    }

}
