// JDK Import statements
import java.io.*;
import java.net.Socket;
// Local package imports
import client.ClientReader;
import client.ClientWriter;

/**
 * The Client class performs the initial CONNECT command logic
 * and then creates the socket for communication to the server.
 * @author jonathan
 * @version 1.0
 * @since 2018-12-11
 */
public class Client {
    /**
     * Blank constructor for the Client class purley for simplicitey's sake
     * @author jonathan
     * @since 2018-12-11
     */
    public Client() { // Blank constructor
    }

    /**
     * Run: The Run method runs the inital CONNECT command
     * and then creates a Socket for communication with the server
     * @author jonathan
     * @since 2018-12-11
     */
    public void Run() {
        // ******************************************
        // *          CONNECT COMMAND CODE          *
        // ******************************************
        //BEFORE CLIENT SOCKET IS CREATED CONNECT COMMAND MUST BE RUN.
        // Prompt user that CONENCT command must be enterd.
        System.out.println("To connect to the server, type CONNECT, followed by the IP adress of the main server and the port you wish to use for communication.");
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        try {
            // Variables to store values for connecting the socket.
            String rootIP;
            int port;
            // Get the user input as the command and split it into the array to parse it
            String cmdInput = stdIn.readLine();
            String[] cmdAndArgs = cmdInput.split(" ");
            // The CONNECT command must be called with 2 arguments - the command, the IP and port number.
            if(cmdAndArgs.length != 3) {
                System.err.println("The CONNECT command must be called like: CONNECT serverIP Port.");
            } else {
                // We can parse the command!
                // Split up command into vairables to be checked.
                String cmd = cmdAndArgs[0];
                rootIP = cmdAndArgs[1];
                String portStr = cmdAndArgs[2];
                // Check to make sure the CONNECT command was enterd
                if(cmd.toUpperCase().equals("CONNECT")) {
                    // Lazy checking to determine if the user enterd in an IP adress.
                    if(rootIP.contains(".") || rootIP.toLowerCase().equals("localhost")) {
                        // Use the Integer.parseInt() function to convert the port string into an int
                        port = Integer.parseInt(portStr);
                        // Create the actual socket for the root server communication.
                        Socket rootSocket = new Socket(rootIP, port);
                        boolean running = true;
                        ClientReader reader = new ClientReader(rootSocket, running);
                        ClientWriter writer = new ClientWriter(rootSocket, running);
                        // Now initalise them as threads
                        Thread readThread = new Thread(reader);
                        Thread writeThread = new Thread(writer);
                        // Start the threads
                        readThread.start();
                        writeThread.start();
                    } else {
                        System.err.println("You must speficy a IP adress as the second arguement.");
                    }

                } else {
                    System.err.println("No other command is supported at this time.");
                }
            }
        } catch(Exception e) {
            System.err.println("An Exception occured during the initial conection process");
            e.printStackTrace();
        }
    }
}
