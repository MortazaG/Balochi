package com.alchemistmoz.balochi.games.memory;

/**
 * Create a MemoryCard object for the MemoryGame.
 */
public class MemoryCard {

    // Store resource ID for the category image
    private int imageResourceID;

    // Store resource ID for the category sound
    private int audioResourceID;

    // Store boolean value for if card has been paired or not
    private boolean paired;

    /**
     * Constructor for handling two arguments.
     * Sets the given image and audio resource ID's.
     *
     * @param imageResourceID Image Resource ID (e.g. R.drawable.xxx).
     * @param audioResourceID Audio Resource ID (e.g. R.raw.xxx).
     */
    public MemoryCard(int imageResourceID, int audioResourceID) {
        this.imageResourceID = imageResourceID;
        this.audioResourceID = audioResourceID;
        paired = false;
    }

    /**
     * Set image resource ID for the Category
     */
    void setImageResourceID(int newImageResourceID) {
        imageResourceID = newImageResourceID;
    }

    /**
     * @return The image resource ID
     */
    int getImageResourceID() {
        return imageResourceID;
    }

    /**
     * @return The audio resource ID
     */
    int getAudioResourceID() {
        return audioResourceID;
    }

    void setPaired() {
        paired = true;
    }

    boolean isPaired() {
        return paired;
    }

}
