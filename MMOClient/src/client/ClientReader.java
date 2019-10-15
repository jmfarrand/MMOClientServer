package client;

//JDK imports
import java.io.DataInputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * The ClientReader class handles reading messages that are sent from the server.
 * It runs in a loop checking that the server has sent a message.<br>
 * This class implements runnable to be able to run this in a Thread
 * @author jonathan
 * @version 1.0
 * @since 2018-12-11
 */
public class ClientReader implements Runnable {
    private Socket socket = null;
    protected boolean running;
    private String data = null;

    /**
     * ClientReader: Creates a new instance of the ClientReader class
     * Passing in the socket to communicate over and a Boolean to decide if the program
     * Is running or not
     * @param socket The socket to communicate over
     * @param runningIn The running Boolean so both threads can quit at the same time
     * @author jonathan
     * @since 2018-12-11
     */
    public ClientReader(Socket socket, boolean runningIn) {
        this.socket = socket;
        this.running = runningIn;
    }

    /**
     * run: This is the code that is runned when the thread is run
     * @author jonathan
     * @since 2018-12-11
     */
    public void run() {
        System.out.println("Enterd client reader thread");
        try {
            DataInputStream dataIn = new DataInputStream(socket.getInputStream());
            System.out.println("Created client input stream.");
            while (running) {
                // Catch the incoming data stream from the socket,
                // read a line and then output it to the console.
                data = dataIn.readUTF();
                // Print the message to the console
                System.out.println("Server message: " + data);
            }
            // When we are done, close the input stream.
            dataIn.close();
        } catch(SocketException se) {
          System.err.println("Socket illegally closed.");
          se.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error in client reader thread:");
            e.printStackTrace();
        }
    }
}
