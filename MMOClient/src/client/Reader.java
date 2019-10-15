package client;

import java.io.DataInput;
import java.io.DataInputStream;
import java.net.Socket;

public class Reader implements Runnable {
    Socket socket = null;
    protected boolean running = true;
    private String data = null;

    public Reader(Socket socket) {
        this.socket = socket;
    }

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
        } catch(Exception e) {
            System.out.println("Error in client reader thread:");
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
