package client.client;

//JDK imports
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * The ClientWriter class handles the user input for sending commands to the server.
 * It runs in a loop checking for user input and then sending the command straight to the server
 * which does all of the command handiling and then sends the results back to the client
 * which reads it using the ClientReader class<br>
 * This class implements runnable to be able to run this in a Thread
 *
 * @author jonathan
 * @version 1.0
 * @since 2018-12-11
 */
public class ClientWriter implements Runnable {
    private Socket socket = null;
    private boolean running;
    private String userInput;

    /**
     * ClientWriter: Constructs a new instance of the ClientWriter class
     * Passing in the socket to output user input to and a Boolean that
     * Determines when the thread should stop in synch with the ClientReader thread
     *
     * @param socket The socket to output user input to
     * @param runningIn A running Boolean that allows it so both threads can quit at the same time
     * @author jonathan
     * @since 2018-12-11
     */
    public ClientWriter(Socket socket, boolean runningIn) {
        this.socket = socket;
        this.running = runningIn;
    }

    /**
     * run: this code is called upon when the thread run method is called
     * @author jonathan
     * @since 2018-12-11
     */
    public void run() {
        System.out.println("Enterd client writer thread");
        try {
            // The outputstream to send data over
            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
            // Fetch user input
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            while(running) {
                // Prompt user to enter string
                System.out.println("Please enter in a string to be sent to the server.");
                userInput = stdIn.readLine();
                // Send the string and then flush the outputstream
                dataOut.writeUTF(userInput);
                dataOut.flush();
            }
            // When we are done, close the output stream.
            dataOut.close();
        } catch(Exception e) {
            System.err.println("Error in client writer thread:");
            e.printStackTrace();
        }
    }

    public synchronized boolean isRunning() {
        return this.running;
    }

    public synchronized void setIsRunning(boolean runningIn) {
        this.running = runningIn;
    }

}
