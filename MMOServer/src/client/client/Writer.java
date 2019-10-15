package client.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Writer implements Runnable {
    Socket socket = null;
    protected boolean running = true;
    String userInput;

    public Writer(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        System.out.println("Enterd client writer thread");
        try {
            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

            // Fetch user input
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            while(running) {
                System.out.println("Please enter in a string to be sent to the server.");
                userInput = stdIn.readLine();
                dataOut.writeUTF(userInput);
                dataOut.flush();
            }
            // When we are done, close the output stream.
            dataOut.close();
        } catch(Exception e) {
            System.out.println("Error in client writer thread:");
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
