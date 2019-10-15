/**
 * Program: The main class for the Client appliation.
 * @author jonathan
 * @version 1.0
 * @since 2018-12-11
 */
public class Program {
    /**
     * Main: The main method that first runs when you run the Client appliation.
     * @param args Command-line argumentes
     * @author jonathan
     * @since 2018-12-11
     */
    public static void main(String[] args) {
        System.out.println("Starting the client...");
        Client client = new Client();
        client.Run();
    }
}
