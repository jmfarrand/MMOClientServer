/**
 * Program: The main class for the Server application
 * @author jonathan
 * @version 1.0
 * @since 2018-12-11
 */
public class Program {
    /**
     * Main: The main method that first runs when you run the Server appliation.
     * @param args Command-line argumentes
     * @author jonathan
     * @since 2018-12-11
     */
    public static void main(String[] args) {
        System.out.println("Starting the ISLAND server...");
        // Create new instance of Server class passing in the server and client connection ports.
        Server server = new Server();
        server.Run();
    }
}
