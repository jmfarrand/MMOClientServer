package NetworkManager;

// Local package import
import MMOFunctions.User;
import MMOFunctions.World;
//Java language imports
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.util.Pair;

/**
 * ServerConnManager: The ServerConnManager class manages the primary root server functionality
 */
public class ServerConnManager {
    // The MMO World
    private World world = null;

    // Variables to hold the client and server ports
    private int clientPort;
    private int serverPort;

    /**
     * Constructs a new instance of the ServerConnManager with the speficed client and server socket ports
     * @param clientPort the client socket port
     * @param serverPort the server socket port
     */
    // CONSTRUCTOR
    public ServerConnManager(int clientPort, int serverPort) {
        //Set up port values
        this.clientPort = clientPort;
        this.serverPort = serverPort;
    }

    /**
     * Run: The main method that is called in the Server class
     * @param worldName the name of the world
     */
    public void Run(String worldName) {
        System.out.println("Enterd ServerConnManager class");
        // First save the server data to the servers.txt file
        writeServersFile();
        // Now set up the first world
        System.out.println("Setting up initial world");
        world = new World(worldName);

        // Start the server listner
        startServerListner();
        // Start the Client listner
        startClientListner();
    }

    /**
     * writeServersFile: This method writes the root server inforamtion to the file servers.txt
     * Code is adapted from: https://www.rgagnon.com/javadetails/java-0542.html
     */
    private void writeServersFile() {
        // create the file to write the data to.
        File file = new File("servers.txt");
        FileWriter fr = null;
        try {
            // write the local pc's ip adress and the server communication port.
            // it writes a ROOTSERVER entry and the intial island entrey as well,
            // because the root server also functions as an island server
            fr = new FileWriter(file);
            InetAddress localhost = InetAddress.getLocalHost();
            fr.write("ROOTSERVER:" + localhost.getHostAddress() + ","+ this.serverPort + "\n");
            fr.write("ISLAND:NUMBER:0," + localhost.getHostAddress() + "," + this.serverPort + "\n");
        } catch(IOException e) {
            System.err.println("Exception while writing World data to file");
            e.printStackTrace();
        } finally {
            // close the FileWriter, an exception is thrown if it breaks
            try {
                fr.close();
            } catch(Exception e) {
                System.err.println("Error while closing FileWriter");
                e.printStackTrace();
            }
        }
    }

    /**
     * startServerListner: Method that manages a conenction between the root server and the island server
     */
    private void startServerListner() {
        // Create a new server listner thread and then start it
        ServerListner serverListner = new ServerListner(serverPort, world);
        Thread serverListnerThread = new Thread(serverListner);
        serverListnerThread.start();
    }

    /**
     * startClientListner: Method to manage connections between the root server and the client
     */
    private void startClientListner() {
        // create a new client listner thread and then start it
        ClientListner clientListner = new ClientListner(clientPort, world);
        Thread clientListnerThread = new Thread(clientListner);
        clientListnerThread.start();
    }
}

