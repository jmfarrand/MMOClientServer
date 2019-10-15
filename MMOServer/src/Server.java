// The main code only fires up the Connection Manager,
// so only import the ServerConnManager class.
import NetworkManager.ServerConnManager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * Server: This class represents the main class for the server and fires up
 * the ServerConnManager which handels the actual server functionality
 */
public class Server {

    public Server() {

    }
    /**
     * Run: The method to run the server
     */
    public void Run() {
        try {
            //Print initial message to notify user to enter IP and port
            System.out.println("Enter the IP of rootserver and port to comunnicate with seperated by space");
            //get the user input
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String input = stdIn.readLine();
            //now parse it
            String[] parsedInput = input.split(" ");
            if(parsedInput.length == 2) {
                String rootIP = parsedInput[0];
                if(rootIP.contains(".") || rootIP.toLowerCase().equals("localhost")) {
                    //when we are done parsing, set up the ServerConnManager and run it.
                    int serverPort = Integer.parseInt(parsedInput[1]);
                    ServerConnManager manager = new ServerConnManager(rootIP, serverPort);
                    manager.Run();
                } else {
                    System.err.println("You must speficy a IP adress.");
                }
            } else {
                System.err.println("You must enter in a IP adress and port number");
            }
        } catch(Exception e) {
            System.err.println("Error while setting up connetion " + e.getMessage());
        }
    }
}
