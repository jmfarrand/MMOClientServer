package MMOFunctions;

// JDK imports
import java.io.*;
import java.util.ArrayList;

/**
 * The island class represnts a world to be hosted by the MMO Root server.
 * It holds various information about the world such as name, size and biome type.
 * @author jonathan
 * @version 1.0
 * @since 2018-12-13
 */
public class Island {
    // Class varibles to hold island attributes
    private String islandName;
    private int sizeX;
    private int sizeY;
    private int sizeZ;
    private ArrayList<User> islandUsers = new ArrayList<>();

    /**
     * This constructs the Island class.
     * @param name The name of the island
     * @param sizeX The size of the island in the X dimension
     * @param sizeY The size of the island in the Y dimension
     * @param sizeZ The size of the island in the Z dimension
     * @author jonathan
     * @since 2018-12-13
     */
    public Island(String name, int sizeX, int sizeY, int sizeZ) {
        this.islandName = name;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }

    /**
     * This special construter creates an island based on an island file passed in.
     * @param file island text file to create
     */
    public Island(String file) {
        try {
            // Read a new file with the name passed in as a paramater
            File islandFile = new File(file);
            BufferedReader fileReader = new BufferedReader(new FileReader(islandFile));
            String st;
            // Loop over all the lines in the file
            while ((st = fileReader.readLine()) != null) {
                // Split by a colon
                String[] stSplit = st.split(":");
                if(stSplit[0].equals("NAME")) {
                    // Parse the island name and set it up
                    String name = stSplit[1];
                    this.islandName = name;
                } else if (stSplit[0].equals("SIZEX")) {
                    // Set up the size X varible
                    String sizeXStr = stSplit[1];
                    int sizeX = Integer.parseInt(sizeXStr);
                    this.sizeX = sizeX;
                } else if (stSplit[0].equals("SIZEY")) {
                    // Set the size y varible
                    String sizeYStr = stSplit[1];
                    int sizeY = Integer.parseInt(sizeYStr);
                    this.sizeY = sizeY;
                } else if (stSplit[0].equals("SIZEZ")) {
                    // finally set the size z value
                    String sizeZStr = stSplit[1];
                    int sizeZ = Integer.parseInt(sizeZStr);
                    this.sizeZ = sizeZ;
                }
            }
        } catch(Exception e) {
            // Print out error if one occured
            System.err.println("Error while creating the island: " + e.getMessage());
        }
    }

    /**
     * getIslandName; Get's the name of the island
     * @return islandName Name of the island
     */
    public String getIslandName() {
        return islandName;
    }

    /**
     * getSizeX: Get's the size of the island in the x dimension
     * @return sizeX The size of the island in X
     */
    public int getSizeX() {
        return sizeX;
    }

    /**
     * getSizeY: Get's the size of the island in the Y dimension
     * @return sizeY The size of the island in Y
     */
    public int getSizeY() {
        return sizeY;
    }

    /**
     * getSizeZ: Get's the size of the island in the Z dimension
     * @return sizeZ The size of the island in Z
     */
    public int getSizeZ() {
        return sizeZ;
    }

    /**
     * writeInfo: Write's inforamtion about the island to a text file.
     * @param islandNum The nuumber of the island that is appendd to the end of the file name
     */
    public void writeInfo(int islandNum) {
        // Create a new file to write to with the island number.
        File file = new File("island" + islandNum + ".txt");
        FileWriter fr = null;
        try {
            // write all the data to the file
            fr = new FileWriter(file);
            fr.write("NAME:" + this.getIslandName() + "\n");
            fr.write("SIZEX:" + this.getSizeX() + "\n");
            fr.write("SIZEY:" + this.getSizeY() + "\n");
            fr.write("SIZEZ:" + this.getSizeZ() + "\n");
        } catch(IOException e) {
            System.out.println("Exception while writing World data to file: " + e.getMessage());
        } finally {
            // now close the FileWriter, if it fails print the error.
            try {
                fr.close();
            } catch(Exception e) {
                System.out.println("Error while closing FileWriter: " + e.getMessage());
            }
        }
    }

    /**
     * addUserToIsland: Add's a user to the island
     * @param user The User that should be added
     */
    public void addUserToIsland(User user) {
        islandUsers.add(user);
    }

    /**
     * removeUserFromIsland: Removs a user from the island
     * @param user The user to remove
     */
    public void removeUserFromIsland(User user) {
        islandUsers.remove(user);
    }

    /**
     * getNumberOfUsers: Get's the total number of users in the island
     * @return islandUsers.size(): The total number of users that are in the island
     */
    public int getNumberOfUsers() {
        return islandUsers.size();
    }

    /**
     * getUser: Get's a spefic user in the island
     * @param userNum The number of user that should be returnd
     * @return islandUsers.get(userNum) The user that is returnd
     */
    public User getUser(int userNum) {
        return islandUsers.get(userNum);
    }

    /**
     * printInfo: prints info about the island to the client
     * @param dataOutputStream the output stream to send data to the client
     */
    public void printInfo(DataOutputStream dataOutputStream) {
        try {
            // send the island information to the client
            dataOutputStream.writeUTF("Island name: " + this.getIslandName());
            dataOutputStream.writeUTF("Island size x: " + Integer.toString(this.getSizeX()));
            dataOutputStream.writeUTF("Island size y: " + Integer.toString(this.getSizeY()));
            dataOutputStream.writeUTF("Island size z: " + Integer.toString(this.getSizeZ()));
            dataOutputStream.writeUTF("Number of users: " + Integer.toString(this.getNumberOfUsers()));
            dataOutputStream.writeUTF("*****Users*****");
            // now print inforamtion about the island users
            for(User user : islandUsers) {
                user.printDetails(dataOutputStream);
            }
        } catch(Exception e) {
            System.err.println("Error while printing island info: "  + e.getMessage());
        }
    }
}
