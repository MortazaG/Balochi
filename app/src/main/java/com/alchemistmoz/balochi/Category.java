package com.alchemistmoz.balochi;

/**
 * Create a category object using one of two constructors.
 *
 * For categories and subcategories, w/ intent to start activity:
 * (int imageResourceID, int audioResourceID, Class className).
 *
 * For other activities where only image and audio is needed:
 * (int imageResourceID, int audioResourceID).
 *
 */
public class Category {

    // Store resource ID for the category image
    private int imageResourceID;

    // Store resource ID for the category sound
    private int audioResourceID;

    // Store the class name correlated with the Category
    private Class className;

    /**
     * Constructor for handling three arguments.
     * Sets the given image and audio resource ID's, as well as the class name of the
     * activity to be opened.
     *
     * @param imageResourceID Image Resource ID (e.g. R.drawable.xxx).
     * @param audioResourceID Audio Resource ID (e.g. R.raw.xxx).
     * @param className Class name for activity to be opened (e.g. xxx.class).
     */
    public Category(int imageResourceID, int audioResourceID, Class className) {
        this.imageResourceID = imageResourceID;
        this.audioResourceID = audioResourceID;
        this.className = className;
    }

    /**
     * Constructor for handling two arguments.
     * Sets the given image and audio resource ID's.
     *
     * @param imageResourceID Image Resource ID (e.g. R.drawable.xxx).
     * @param audioResourceID Audio Resource ID (e.g. R.raw.xxx).
     */
    public Category(int imageResourceID, int audioResourceID) {
        this.imageResourceID = imageResourceID;
        this.audioResourceID = audioResourceID;
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
    public int getAudioResourceID() {
        return audioResourceID;
    }

    /**
     * @return The stored className
     */
    public Class getClassName() {
        return className;
    }

}
