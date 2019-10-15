package MMOFunctions;

import java.io.DataOutputStream;

/**
 * The user class represents a user of the MMO Applicaion
 */
public class User {
    // The user data
    private String username;
    private int xPos;
    private int yPos;
    private int zPos;

    /**
     * Sets up the user data
     * @param username The username
     * @param initialXPos The initial X position of the user
     * @param initialYPos The initial Y position of the user
     * @param initialZPos The initial Z position of the user
     */
    public User(String username, int initialXPos, int initialYPos, int initialZPos) {
        this.username = username;
        this.xPos = initialXPos;
        this.yPos = initialYPos;
        this.zPos = initialZPos;
    }

    /**
     * Constructer to create new user just by a username.
     * Used during setting a user's position
     * @param username The username
     */
    public User(String username) {
        this.username = username;
        this.xPos = 0;
        this.yPos = 0;
        this.zPos = 0;
    }
    /**
     * getUsername; Get's the username of the user
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * setXPos: Set's the X potision of the user
     * @param xPos The x potition of the user
     */
    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    /**
     * getXPos: Get's the X potision of the user
     * @return xPos The x potition of the user
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * setYPos: Set's the y potision of the user
     * @param yPos The Y potition of the user
     */
    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    /**
     * getYPos: Get's the Y potision of the user
     * @return xPos The y potition of the user
     */
    public int getyPos() {
        return yPos;
    }

    /**
     * setZPos: Set's the Z potision of the user
     * @param zPos The Z potition of the user
     */
    public void setzPos(int zPos) {
        this.zPos = zPos;
    }

    /**
     * getZPos: Get's the Z potision of the user
     * @return zPos The z potition of the user
     */
    public int getzPos() {
        return zPos;
    }

    /**
     * printDetails: Print's the details about the current user
     */
    public void printDetails(DataOutputStream dataOutputStream) {
        try {
            dataOutputStream.writeUTF("Username: " + this.getUsername());
            dataOutputStream.writeUTF("xPos " + this.getxPos());
            dataOutputStream.writeUTF("yPos " + this.getyPos());
            dataOutputStream.writeUTF("zPos " + this.getzPos());
        } catch(Exception e) {
            System.err.println("Exception while printing user details");
            e.printStackTrace();
        }
    }
}
