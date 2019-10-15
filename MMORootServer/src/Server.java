// The main code only fires up the Connection Manager,
// so only import the ServerConnManager class.
import NetworkManager.ServerConnManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Server: This class represents the main class for the server and fires up
 * the ServerConnManager which handels the actual server functionality
 * @author jonathan
 * @version 1.0
 * @since 2018-12-13
 */
public class Server {
    private int serverPort;
    private int clientPort;

    /**
     * Contstructor for the server class
     * @param serverPort The port to connect to island servers on
     * @param clientPort The port to connect to clients on
     * @author jonathan
     * @since 2018-12-13
     */
    public Server(int serverPort, int clientPort) {
        this.serverPort = serverPort;
        this.clientPort = clientPort;
    }

    /**
     * Run: The method to run the server
     * @author jonathan
     * @since 2018-12-13
     */
    public void Run() {
        try {
            System.out.println("Please enter in the name of the world");
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String worldName = stdIn.readLine();
            System.out.println("Setting up server with Client port 12000 and Server port 11000");
            // Create the ServerConnManager and then run it.
            ServerConnManager manager = new ServerConnManager(clientPort, serverPort);
            manager.Run(worldName);
        } catch(Exception e) {
            System.err.println("Error while setting up root server: " + e.getMessage());
        }
    }
}
