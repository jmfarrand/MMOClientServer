
package NetworkManager;

import MMOFunctions.User;
import MMOFunctions.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ClientListner: This class (started as a thread) is responsible for
 * opening a ServerSocket that listns for client conenctions.
 * When it finds one, it fires up a clientthread that manages the connection.
 */
public class ClientListner implements Runnable {
    // the port to communicate with the client over
    private int clientPort;
    // the server socket that listns for client conenctions
    private ServerSocket serverSocket;
    // the MMO world
    private World world;

    // Construct a new ClientListner with the port to listn on and the MMO's world.
    public ClientListner(int clientPort, World world) {
        this.clientPort = clientPort;
        this.world = world;
    }

    public void run() {
        try {
            // set up the server socket
            serverSocket = new ServerSocket(clientPort);
            while(true) {
                // listne for client conenctions
                System.out.println("Waiting for client to connect...");
                Socket serverSocketSocket = serverSocket.accept();
                // when a client connection is found, start a new thread to manage the conection
                ClientThread clientThread = new ClientThread(serverSocketSocket, world);
                Thread clientThreadThread = new Thread(clientThread);
                clientThreadThread.start();
            }
        } catch(Exception e) {
            System.err.println("Error with the client Listner");
            e.printStackTrace();
        }
    }
}
