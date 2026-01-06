package OCSF.server.main;

import OCSF.server.networking.AirportServer;

import java.io.IOException;
import java.util.Scanner;

public class ServerApplication {

    private static final int PORT = 3000; // Must match the client's port

    public static void main(String[] args) {
        AirportServer server = new AirportServer(PORT);

        try {
            server.listen(); // Start the OCSF listener thread
            System.out.println("Server started successfully.");
            System.out.println("Listening for connections on port " + PORT);
            System.out.println("Press ENTER to stop the server...");

            // Keep the main thread alive until user wants to stop
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

            server.close();
            System.out.println("Server stopped.");

        } catch (IOException e) {
            System.err.println("Could not start server. Port " + PORT + " might be in use.");
            e.printStackTrace();
        }
    }
}