package MMOFunctions;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * World: The world class represents a world in the MMO game.
 * It holds various attributes useful to the world such as a
 * name, list of islands and the size.
 */
public class World {
    // Class varibles to hold information about the world
    private String worldName;
    private ArrayList<Island> worldIslands = new ArrayList<>();
    private int sizeX;
    private int sizeY;
    private int sizeZ;

    /**
     * World: Constructs a new instance of the World class
     * taking the name of the world as a paremeter.
     * It also sets up the inital size of the world to the 128x128x128 block.
     * @param nameIn The name of the world to be set
     */
    public World(String nameIn) {
        this.worldName = nameIn;
        // Create the first island with the 128x128x128 block.
        System.out.println("Setting up initial Island");
        Island firstIsland = new Island("firstIsland", 128, 128, 128);
        // Set the intial world values based on the first island
        this.sizeX = firstIsland.getSizeX();
        this.sizeY = firstIsland.getSizeY();
        this.sizeZ = firstIsland.getSizeZ();
        // Now add the island to the list
        worldIslands.add(firstIsland);
        System.out.println("The first island has been added");
        // then save the island to the file
        firstIsland.writeInfo(0);
        System.out.println("The first island has been saved");
    }

    /**
     * getWorldName: Get's the current name of the world.
     * @return worldName the name of the world
     */
    public String getWorldName() {
        return worldName;
    }

    /**
     * setSizeX: Set's the total size of the world in the
     * X coordinate.
     * @param sizeX The x coordinate size of the world
     */
    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    /**
     * getSizeX: Get's the total size of the world in the
     * X coordinate.
     * @return sizeX The x coordinate size of the world
     */
    public int getSizeX() {
        return sizeX;
    }

    /**
     * setSizeY: Set's the total size of the world in the
     * Y coordinate.
     * @param sizeY The y coordinate size of the world
     */
    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    /**
     * getSizeY: Get's the total size of the world in the
     * Y coordinate.
     * @return sizeY The y coordinate size of the world
     */
    public int getSizeY() {
        return sizeY;
    }

    /**
     * setSizeZ: Set's the total size of the world in the
     * Z coordinate.
     * @param sizeZ The Z coordinate size of the world
     */
    public void setSizeZ(int sizeZ) {
        this.sizeZ = sizeZ;
    }

    /**
     * getSizeZ: Get's the total size of the world in the
     * Z coordinate .
     * @return sizeZ The z coordinate size of the world
     */
    public int getSizeZ() {
        return sizeZ;
    }

    /**
     * writeData: Write's the world data to a file named "world.txt"
     */
    public void writeData() {
        // create the file to be written to
        File file = new File("world.txt");
        FileWriter fr = null;
        try {
            // start writing to the file.
            fr = new FileWriter(file);
            // first, write the world information to the file
            fr.write("World Name: " + this.getWorldName() + "\n");
            fr.write("Total world size X: " + this.getSizeX() + "\n");
            fr.write("Total world size Y: " + this.getSizeY() + "\n");
            fr.write("Total world size Z: " + this.getSizeZ() + "\n");
            fr.write("===== STARTING ISLAND INFO =====" + "\n");
            // Loop over each island writing out information to each one.
            for(int i = 0; i < worldIslands.size(); i++) {
                Island tmp = worldIslands.get(i);
                fr.write("Island num: " + i + "\n");
                fr.write("Island name: " + tmp.getIslandName() + "\n");
                fr.write("Island size X: " + tmp.getSizeX() + "\n");
                fr.write("Island size Y: " + tmp.getSizeY() + "\n");
                fr.write("Island size Z: " + tmp.getSizeZ() + "\n");
            }

        } catch(IOException e) {
            System.out.println("Exception while writing World data to file");
            e.printStackTrace();
        } finally {
            // now close the FileWriter, an exception is thrown if an error occurs
            try {
                fr.close();
            } catch(Exception e) {
                System.out.println("Error while closing FileWriter");
                e.printStackTrace();
            }
        }
    }

    /**
     * saveIsland: Saves the specifed island information to the root server
     * @param islandNum The island whose data to dave
     */
    public void saveIsland(int islandNum) {
        worldIslands.get(islandNum).writeInfo(islandNum);
    }

    /**
     * getNumberOfIslands: Returns the number of islands in the world.
     * @return The number of worlds in the world.
     */
    public int getNumberOfIslands() {
        return worldIslands.size();
    }

    /**
     * addIsland: Adds the island to the world and updates the total size of the world as well.
     * @param island The island to add to the world.
     */
    public void addIsland(Island island) {
        worldIslands.add(island);
        this.sizeX += island.getSizeX();
        this.sizeY += island.getSizeY();
        this.sizeZ += island.getSizeZ();
    }

    /**
     * addUserToInitialIsland: Add's the first user to the island.
     * @param user The user whose should be added
     */
    public void addUserToInitialIsland(User user) {
        worldIslands.get(0).addUserToIsland(user);
    }

    /**
     * cmdList: The implementation of the LIST command.
     * @param sender The data output stream to send data across
     */
    public void cmdList(DataOutputStream sender) {
        try {
            // Send the world information to the client
            sender.writeUTF("World name: " + this.getWorldName());
            sender.writeUTF("World size x: " + Integer.toString(this.getSizeX()));
            sender.writeUTF("World size y: " + Integer.toString(this.getSizeY()));
            sender.writeUTF("World size z: " + Integer.toString(this.getSizeZ()));
            sender.writeUTF("Number of islands in the world: " + Integer.toString(this.getNumberOfIslands()));
            // now send the island spefic info
            sender.writeUTF("*****Islands*****");
            // call the island's own printInfo method that sends data to the client.
            for(Island island : worldIslands) {
                island.printInfo(sender);
            }
        } catch(Exception e) {
            System.err.println("Exception whilst running LIST command.");
            e.printStackTrace();
        }
    }

    /**
     * cmdTransfer: Transfers a user from one island to another
     * @param current the user to transfer
     * @param islandFrom the island the user should currentlay be on
     * @param islandTo  the island the user wants to transfer to
     */
    public void cmdTransfer(User current, int islandFrom, int islandTo) {
        worldIslands.get(islandFrom).removeUserFromIsland(current);
        worldIslands.get(islandTo).addUserToIsland(current);
    }

    /**
     * getIsland: Get's the speficed island in the world.
     * @param islandNum The island that should be fetched from the list
     * @return worldIslands.get(islandNum) The Island that is fetched
     */
    public Island getIsland(int islandNum) {
        return worldIslands.get(islandNum);
    }
}
