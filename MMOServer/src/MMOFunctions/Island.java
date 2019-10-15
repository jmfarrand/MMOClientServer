package MMOFunctions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
/**
 * The island class represnts a world to be hosted by the MMO Root server.
 * It holds various information about the world such as name, size and biome type.
 * @author jonathan
 * @version 1.0
 * @since 2018-12-13
 */
public class Island {
    // Class varibles to hold world attributes
    private String islandName;

    private int sizeX;
    private int sizeY;
    private int sizeZ;
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
     * getIslandName; Get's the name of the island
     * @return islandName Name of the island
     */
    public String getIslandName() {
        return islandName;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }
    /**
     * getSizeX: Get's the size of the island in the x dimension
     * @return sizeX The size of the island in X
     */
    public int getSizeX() {
        return sizeX;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }
    /**
     * getSizeY: Get's the size of the island in the Y dimension
     * @return sizeY The size of the island in Y
     */
    public int getSizeY() {
        return sizeY;
    }

    public void setSizeZ(int sizeZ) {
        this.sizeZ = sizeZ;
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
        File file = new File("island" + islandNum + ".txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write("NAME:" + this.getIslandName() + "\n");
            fr.write("SIZEX:" + this.getSizeX() + "\n");
            fr.write("SIZEY:" + this.getSizeY() + "\n");
            fr.write("SIZEZ:" + this.getSizeZ() + "\n");
        } catch(IOException e) {
            System.out.println("Exception while writing World data to file");
            e.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch(Exception e) {
                System.out.println("Error while closing FileWriter");
                e.printStackTrace();
            }
        }
    }
}
