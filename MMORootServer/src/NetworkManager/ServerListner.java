package NetworkManager;

import MMOFunctions.World;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * ServerListner: This thread sets up a socket that listnes for
 * Island servers to conenct to. When it finds one, it sets up
 * a thread to manage the conenction.
 */
public class ServerListner implements Runnable {
    // the port to communicate over
    private int serverPort;
    // the MMO world
    private World world;
    ServerSocket serverSocket;
    //sets up the serverlistner with the port and the mmo world
    public ServerListner(int serverPort, World world) {
        this.serverPort = serverPort;
        this.world = world;
    }

    public void run() {
        try {
            //set up the serversocket
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while(true) {
                //listen for any incoming server connections.
                System.out.println("Waiting for server to connect...");
                Socket serverSocketSocket = serverSocket.accept();
                //if a server conenction is found, start up a new thread to manage it
                ServerThread serverThread = new ServerThread(serverSocketSocket, world);
                Thread serverThreadThread = new Thread(serverThread);
                serverThreadThread.start();
            }
        } catch(Exception e) {
            System.err.println("Error with the Server Listner");
            e.printStackTrace();
        }
    }
}
