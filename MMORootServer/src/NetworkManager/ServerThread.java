package NetworkManager;

import MMOFunctions.Island;
import MMOFunctions.User;
import MMOFunctions.World;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * ServerThread: The class that manages conenction between the root server and island servers
 */
public class ServerThread implements Runnable {
    private Socket serverSocket;
    private int numIslands;
    private World world;

    public ServerThread(Socket serverSocket, World world) {
        this.serverSocket = serverSocket;
        numIslands = 0;
        this.world = world;
    }

    /**
     * run: The method to run when the ServerThread thread starts
     */
    public void run() {
      try {
          //print message to notify that server has connected
            System.out.println("Server connected!");
            //set up the network I/O streams
            DataInputStream dataIn = new DataInputStream(serverSocket.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(serverSocket.getOutputStream());
            //check to see if the island server requests the servers.txt file
            if(dataIn.readUTF().equals("sendserversfile")) {
                //now send the file to the island server
                sendFileToIslandServer("servers.txt", dataOut);
                dataOut.flush();
            }
            //loop to check if the server is sending the servers.txt file back to the root.
            boolean done = false;
            while(!done) {
                if(dataIn.available() > 0) {
                    if(dataIn.readUTF().equals("reciveserversfile")) {
                        //recive the servers.txt file
                        System.out.println("Reciving servrs.txt");
                        reciveFileFromIslandServer("servers.txt", dataIn);
                        done = true;
                    }
                }
            }

            //now parse the newly recived servers.txt file to get the island number.
            File file = new File("servers.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            int islandNum = 0;
            //read the file line by line
            while ((st = br.readLine()) != null) {
                //splot by the colon
                String[] stSplit = st.split(":");
                //if we are at the ISLAND server entrey set the islandNum to be the number it sent over
                if(stSplit[0].equals("ISLAND")) {
                    String[] stSplit2 = stSplit[2].split(",");
                    islandNum = Integer.parseInt(stSplit2[0]);
                }
            }

            //another loop to check if the island server is sending over the island file
            boolean done2 = false;
            while(!done2) {
                if(dataIn.available() > 0) {
                    if(dataIn.readUTF().equals("reciveislandfile")) {
                        System.out.println("Reciving island.txt");
                        reciveFileFromIslandServer("island" + islandNum + ".txt", dataIn);
                        done2 = true;
                    }
                }
            }
            //then create a new island based on this information, and then addit to the world.
            Island island = new Island("island" + islandNum + ".txt");
            world.addIsland(island);
            //INITIAL INFORMATION EXCHANGE HAS NOW BEEN COMPLETED!

          //this is the main loop to parse commands.
            boolean running = true;
            while(running) {
                if (dataIn.available() > 0) {
                    System.out.println("Reciving data from island server");
                    //store the user input
                    String input = dataIn.readUTF();
                    System.out.println(input);
                    //the command is parsed as upper case
                    String cmd = input.toUpperCase();
                    //MAIN SERVER COMMAND PARSING
                    switch (cmd) {
                        case "QUIT":
                            if(dataIn.available() > 0) {
                                System.out.println(dataIn.readUTF());
                            }
                            running = false;
                            break;
                        case "ISLANDINFO":

                        default:
                            dataOut.writeUTF("That command isn't implemeanted.");
                            dataOut.flush();
                            break;
                    }
                }
            }
            //when we quit, saftley exit by closing the I/O streams and the socket
            System.out.println("Closing I/O streams and the socket.");
            dataOut.flush();
            dataOut.close();
            dataIn.close();
            serverSocket.close();
        } catch (Exception e) {
            System.err.println("Exception while handling network code: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * sendFileToIslandServer: Send's a file to the island server
     * @param file The name of the file to be sent
     * @param dataOutputStream The output stream to send the file over
     */
    private void sendFileToIslandServer(String file, DataOutputStream dataOutputStream) {
        try {
            //create a new file with the name of the file paramaeter
            File fileToSend = new File(file);
            StringBuilder fileAsStringBuilder = new StringBuilder((int)fileToSend.length());
            String fileAsString;
            //create a new scanner to build the string to send the file over.
            try (Scanner scanner = new Scanner(fileToSend)){
                //for every new line in the file, add it to the string
                while(scanner.hasNextLine()) {
                    fileAsStringBuilder.append(scanner.nextLine() + "\n");
                }
            }
            //then set the string to the built string
            fileAsString = fileAsStringBuilder.toString();
            //now actually send the file to the island server
            dataOutputStream.writeUTF(fileAsString);
            //print message of confirmation
            System.out.println("The file " + file + " has been sent.");
        } catch (Exception e) {
            System.err.println("Error while sending file: " + e.getMessage());
        }

    }

    /**
     * reciveFileFromIslandServer: Reciv's a file from the island server
     * @param file The name of the file that should be created when the file is recived
     * @param dataInputStream input stream to recive the file from
     */
    private void reciveFileFromIslandServer(String file, DataInputStream dataInputStream) {
        try {
            //create a blank file with the name of the file to be recived
            File fileToBeRecivd = new File(file);
            //Recive the file as a string from the island server
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
