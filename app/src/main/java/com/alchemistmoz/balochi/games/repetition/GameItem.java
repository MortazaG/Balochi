package com.alchemistmoz.balochi.games.repetition;

/**
 * Create an item object to be used for the game.
 */
public class GameItem {

    private int imageResourceId;
    private int audioResourceId;

    // Store boolean value for if item has been selected or not
    private boolean isSelected;

    /**
     * Constructor for handling two arguments.
     * Sets the given image and audio resource ID's.
     *
     * These are usually the in game items.
     *
     * @param imageResourceID Image Resource ID (e.g. R.drawable.xxx).
     * @param audioResourceID Audio Resource ID (e.g. R.raw.xxx).
     */
    public GameItem(int imageResourceID, int audioResourceID) {
        this.imageResourceId = imageResourceID;
        this.audioResourceId = audioResourceID;
        isSelected = false;
    }

    /**
     * Constructor for handling one argument.
     * Sets the given audio resource ID to be used.
     *
     * These are usually the intro sounds.
     *
     * @param audioResourceId Audio Resource ID (e.g. R.raw.xxx).
     */
    public GameItem(int audioResourceId) {
        this.imageResourceId = 0;
        this.audioResourceId = audioResourceId;

    }

    /**
     * Set image resource ID for the GameItem.
     *
     * @param newImageResourceId - Image resource ID e.g. R.drawable.xxx.
     */
    public void setImageResourceID(int newImageResourceId) {
        imageResourceId = newImageResourceId;
    }

    /**
     * @return The image resource ID
     */
    public int getImageResourceID() {
        return imageResourceId;
    }

    /**
     * Set audio resource ID for the GameItem.
     *
     * @param newAudioResourceId - Image resource ID e.g. R.drawable.xxx.
     */
    public void setAudioResourceID(int newAudioResourceId) {
        audioResourceId = newAudioResourceId;
    }

    /**
     * @return The audio resource ID
     */
    public int getAudioResourceID() {
        return audioResourceId;
    }

    /**
     * Set the current selected state.
     *
     * @param state - true / false
     */
    public void setSelected(boolean state) {
        isSelected = state;
    }

    /**
     * @return current selected state.
     */
    public boolean isSelected() {
        return isSelected;
    }
}
