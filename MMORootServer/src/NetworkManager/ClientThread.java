package NetworkManager;

import MMOFunctions.User;
import MMOFunctions.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * ClientThread: This class represents a thread to manage each client conenction.
 */
public class ClientThread implements Runnable {
    // the port to communicate with the client.
    private Socket clientSocket;
    private World world;

    public ClientThread(Socket clientSocket, World world) {
        this.clientSocket = clientSocket;
        this.world = world;
    }

    /**
     * run: The method to run when the ClientThread thread starts
     */
    public void run() {
        try {
            // print message to signal client connection
            System.out.println("Client connected!");
            // set up the I/O network streams
            DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());
            // Varibles for the username and the first user
            String username;
            User user;
            //notify client to enter in a username
            dataOut.writeUTF("Please enter in a username:");
            dataOut.flush();
            //recive the username from the client
            username = dataIn.readUTF();
            //create the user based on the data
            user = new User(username, 0, 0, 0);
            //finally, add the user to the first island.
            world.addUserToInitialIsland(user);

            //save the world's data
            world.writeData();
            //save all of the islands in the world, aswell
            for (int i = 0; i < world.getNumberOfIslands(); i++) {
                world.saveIsland(i);
            }

            //varibles to hold user input
            String strInput;
            String[] strInputArray;
            //variables for the TRANSFER command
            int islandNumFrom = 0;
            int islandNumTo = 0;

            // **************************
            // *    CLIENT COMMANDS     *
            // **************************
            dataOut.writeUTF("Enter in a command...");
            dataOut.writeUTF("Supported commands are: SETPOSITION, TRANSFER, ISLANDINFO, LIST & QUIT");
            boolean running = true;
            while (running) {
                if (dataIn.available() > 0) {
                    System.out.println("Reciving data from client");
                    dataOut.writeUTF("Enter in a command...");
                    dataOut.writeUTF("Supported commands are: SETPOSITION, TRANSFER, ISLANDINFO, LIST & QUIT");
                    //hold the input in the strInput
                    strInput = dataIn.readUTF();
                    System.out.println(strInput);

                    //check to see if the user enterd a space.
                    //this is only used for the TRANSFER command
                    if(strInput.contains(" ")) {
                        // now split it
                        strInputArray = strInput.split(" ");
                        //SETPOSITION
                        if(strInputArray.length == 4) { //SETPOSITION only takes 3 arguments
                            if(strInputArray[0].toUpperCase().equals("SETPOSITION")) {
                                //parse x y and z coordinates.
                                int xPos = Integer.parseInt(strInputArray[1]);
                                int yPos = Integer.parseInt(strInputArray[2]);
                                int zPos = Integer.parseInt(strInputArray[3]);
                                //and then set the current user to those coordinates.
                                user.setxPos(xPos);
                                user.setyPos(yPos);
                                user.setzPos(zPos);
                            } else {
                                dataOut.writeUTF("Please specify the x y and z coordinate to set");
                            }
                        } else if(strInputArray.length == 3) { //the TRANSFER command only works with 3 argumentes
                            // assign the numbers to the user input values
                            islandNumFrom = Integer.parseInt(strInputArray[1]);
                            islandNumTo = Integer.parseInt(strInputArray[2]);
                            System.out.println(strInputArray[1] + strInputArray[2]);
                            //check to see if the user actually enterd the TRANSFER command
                            if(strInputArray[0].toUpperCase().equals("TRANSFER")) {
                                //now run the command
                                world.cmdTransfer(user, islandNumFrom, islandNumTo);
                                //confirmation message to let user know command has finished execution
                                dataOut.writeUTF("Done.");
                                dataOut.flush();
                            } else {
                                dataOut.writeUTF("Please specify the island number to transfer to");
                            }
                        } else if(strInputArray.length == 2) { //ISLANDINFO - takes 1 argument
                            if(strInputArray[0].toUpperCase().equals("ISLANDINFO")) {
                                //get the island number as enterd by user
                                int islandNum = Integer.parseInt(strInputArray[1]);
                                //and now use it to send info to the client
                                world.getIsland(islandNum).printInfo(dataOut);
                            } else {
                                dataOut.writeUTF("Please specify a island to print the info of");
                            }
                        } else {
                            dataOut.writeUTF("That command isn't implemeanted.");
                        }
                        //the LIST command doesnt take any arguements
                    } else if(strInput.toUpperCase().equals("LIST")) {
                        dataOut.writeUTF("====== Begin list command ======");
                        dataOut.flush();
                        //actually run the LIST command
                        world.cmdList(dataOut);
                        //the QUIT takes no arguments as well
                    } else if(strInput.toUpperCase().equals("QUIT")) {
                        running = false; //end the main loop
                    } else {
                        dataOut.writeUTF("That command isn't implemeanted.");
                    }
                }
            }
            // when we have ended the main loop, close the I/O streams and the client socket.
            System.out.println("Closing I/O streams and the socket.");
            dataOut.flush();
            dataOut.close();
            dataIn.close();
            clientSocket.close();
        } catch (Exception e) {
            System.err.println("Exception while handling network code: " + e.toString());
        }
    }
}
