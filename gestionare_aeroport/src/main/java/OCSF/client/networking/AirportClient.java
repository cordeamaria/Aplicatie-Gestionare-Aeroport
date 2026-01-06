package OCSF.client.networking;

import OCSF.client.AbstractClient;
import networking.Message;

import java.io.IOException;

public class AirportClient extends AbstractClient {

    private static AirportClient instance;

    // We store the response here temporarily
    private Object lastResponse;
    private boolean isWaiting = false;

    // Singleton Pattern: Private constructor
    private AirportClient(String host, int port) {
        super(host, port);
        try {
            openConnection();
            System.out.println("Connected to server: " + host + ":" + port);
        } catch (IOException e) {
            System.err.println("Failed to connect to server!");
            e.printStackTrace();
        }
    }

    // Singleton Pattern: Public accessor
    public static AirportClient getInstance() {
        if (instance == null) {
            // Ensure this matches your ServerApplication port
            instance = new AirportClient("localhost", 3000);
        }
        return instance;
    }

    /**
     * This method is called automatically by OCSF when the Server sends a message back.
     * We use it to wake up the thread waiting in sendRequest().
     */
    @Override
    protected void handleMessageFromServer(Object msg) {
        if (msg instanceof Message) {
            synchronized (this) {
                this.lastResponse = ((Message) msg).getObject();
                this.isWaiting = false;
                notify(); // WAKE UP the controller thread!
            }
        }
    }

    /**
     * SEND AND WAIT (Synchronous)
     * This method sends a request and BLOCKS until the response arrives.
     * This makes it compatible with your existing Controller logic.
     * * @param type The command (e.g., "LOGIN")
     * @param data The payload (e.g., User object)
     * @return The response object from the server (e.g., User found, List<Zbor>, etc.)
     */
    public Object sendRequest(String type, Object data) {
        synchronized (this) {
            try {
                // 1. Send the message
                sendToServer(new Message(type, data));

                // 2. Wait for the response
                isWaiting = true;
                while (isWaiting) {
                    wait(); // Blocks here until handleMessageFromServer calls notify()
                }

                // 3. Return the data we received
                return lastResponse;

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    // Hook method called when connection closes
    @Override
    protected void connectionClosed() {
        System.out.println("Connection to server lost.");
    }
}