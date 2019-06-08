package com.alchemistmoz.balochi.misc;

/**
 * Utilities class with various useful methods to be used
 * across different games.
 *
 */
public final class GameUtils {

    // Allows for touch events to be temporarily disabled
    private static boolean touchEnabled = true;

    /**
     * Prevent user from instantiating the class.
     */
    private GameUtils() {
        // Empty
    }

    /**
     * @param enabled - Status of touch events.
     */
    private static void setTouchEnabled(boolean enabled) {
        touchEnabled = enabled;
    }

    /**
     * @return current status of touch events.
     */
    private static boolean isTouchEnabled() {
        return touchEnabled;
    }
}
