package NetworkManager;

// Local package import
import MMOFunctions.Island;
//Java language imports
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * ServerConnManager: The ServerConnManager class manages the primary island server functionality
 */
public class ServerConnManager {
    // Variables to hold the client and server ports
    private int serverPort;
    private String rootIP;
    //the socket that holds the connection to the root server
    private Socket rootServer = null;

    // CONSTRUCTOR
    public ServerConnManager(String rootIP, int serverPort) {
        //the ip of the root server
        this.rootIP = rootIP;
        //Set up port values
        this.serverPort = serverPort;
    }

    public void Run() {
        //start the server listner
        System.out.println("Starting server listner");
        startServerListner();
    }

    /**
     * startServerListner: The main method that manages all communication between the island server and the root server
     */
    private void startServerListner() {
        try {
            //set up the root server communciation socket
            rootServer = new Socket(this.rootIP, this.serverPort);
            //set up the socket I/O streams
            DataInputStream dataIn = new DataInputStream(rootServer.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(rootServer.getOutputStream());

            System.out.println("Recivng servers.txt from root");
            //send message to root requesting the servers.txt file to be sent.
            dataOut.writeUTF("sendserversfile");
            dataOut.flush();

            //actually recive the file
            reciveFile("servers.txt", dataIn);

            //parse the recived servers.txt file to determine which island we need to setup
            File file = new File("servers.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            int islandNum = 0;
            //read the file line by line
            while ((st = br.readLine()) != null) {
                String[] stSplit = st.split(":");
                //we are only interested in the parts spefcing ISLAND's
                if(stSplit[0].equals("ISLAND")) {
                    String[] stSplit2 = stSplit[2].split(",");
                    //set the island number to be the previous one incremented by 1.
                    islandNum = (Integer.parseInt(stSplit2[0]) + 1);
                }
            }
            //print out message notifiying user of what island we are
            System.out.println("We are island: " + islandNum);
            System.out.println("Now writing island info to servers.txt");
            //now write this inforamtion, along with ip adress and the port number to the servers.txt file.
            FileWriter fr = new FileWriter(file, true);
            InetAddress localhost = InetAddress.getLocalHost();
            fr.write("ISLAND:NUMBER:" + (islandNum) + "," + localhost.getHostAddress() + "," + this.serverPort + "\n");
            fr.close();

            //get the user to enter the island name. the size is automatically added.
            System.out.println("Please enter in the island infromation");
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String islandName = stdIn.readLine();
            //create the new island
            Island newIsland = new Island(islandName, 128, 128, 128);
            System.out.println("Now writing island info to file...");
            //and save the island info to the file
            newIsland.writeInfo(islandNum);

            //then send the modified servers.txt file back to the root.
            System.out.println("Now sending servers info to root server");
            dataOut.writeUTF("reciveserversfile");
            sendFile("servers.txt", dataOut);

            //then send the island info file to the root server
            System.out.println("Now sending island info to root server");
            dataOut.writeUTF("reciveislandfile");
            sendFile("island" + islandNum + ".txt", dataOut);

            //enter a loop to determine if the island server should be quit.
            System.out.println("Enter QUIT to disconnect from the root server");
            boolean running = true;
            String input;
            while(running) {
                input = stdIn.readLine();
                if(input.toUpperCase().equals("QUIT")) {
                    System.out.println("Quitting...");
                    dataOut.writeUTF("quit");
                    running = false;
                }
            }
            //when the island server quits, make sure to close the I/O streams and the main socket.
            dataIn.close();
            dataOut.close();
            rootServer.close();
        } catch (Exception e) {
            System.err.println("Exception while handling network code: " + e.toString());
        }
    }

    /**
     * sendFile Send's a file to the root server
     * @param file The name of the file that should be sent
     * @param dataOutputStream The output stream to send the file to
     */
    private void sendFile(String file, DataOutputStream dataOutputStream) {
        try {
            //create a new file with the name of the file paramaeter
            File fileToSend = new File(file);
            StringBuilder fileAsStringBuilder = new StringBuilder((int)fileToSend.length());
            String fileAsString;
            //create a new scanner to build the string to send the file over.
            try (Scanner scan = new Scanner(fileToSend)) {
                //for every new line in the file, add it to the string
                while(scan.hasNextLine()) {
                    fileAsStringBuilder.append(scan.nextLine() + "\n");
                }
            }
            //then set the string to the built string
            fileAsString = fileAsStringBuilder.toString();
            //now actually send the file to the root server
            dataOutputStream.writeUTF(fileAsString);
            //print message of confirmation
            System.out.println("The file " + file + " has been sent.");
        } catch (Exception e) {
            System.err.println("Error while sending file: " + e.getMessage());
        }
    }

    /**
     * reciveFile Reciv's a file from the root server
     * @param file The name of the file that should be created
     * @param dataInputStream The input stream to read the file from
     */
    private void reciveFile(String file, DataInputStream dataInputStream) {
        try {
            //create a blank file with the name of the file to be recived
            File fileToBeRecivd = new File(file);
            //Recive the file as a string from the root server
            String fileToBeRecivdString = dataInputStream.readUTF();
            //Using the PrintWriter, convert the file from the string to the file
            try (PrintWriter out = new PrintWriter(fileToBeRecivd)) {
                out.print(fileToBeRecivdString);
            }
            //print message of confirmation
            System.out.println("The file " + file + " has been recived.");
        } catch (Exception e) {
            System.err.println("Exception while reciving file. " + e.getMessage());
        }
    }
}


