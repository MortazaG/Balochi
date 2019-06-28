package com.alchemistmoz.balochi.games.repetition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.misc.GameUtils;
import com.alchemistmoz.balochi.misc.SoundPlayback;


/**
 * This game is run in conjunction with SoundPlayback and GameUtils.
 * The layout of the game is defined in layout_face_game.xml.
 *
 * Game description: Clickable picture of the face is presented to the user.
 *                   Clicking a specific part of the face will initiate playback
 *                   of the word for that part.
 *
 * The game is implemented in the Activity class from which it will be run.
 *
 * How to implement the game:
 *
 * - Use the following in onCreate():
 *
 *         // Find and store the image views to be used for the game
 *         ImageView imageView = findViewById(R.id.face_image);
 *         ImageView imageAreasView = findViewById(R.id.face_image_areas);
 *
 *         // Initiate a new round of the counting game
 *         faceGame = new FaceGame(imageAreasView);
 *
 *         imageView.setOnTouchListener(new View.OnTouchListener() {
 *             // @Override
 *             public boolean onTouch(View view, MotionEvent motionEvent) {
 *
 *                 faceGame.selectItem(view, motionEvent);
 *                 return true;
 *             }
 *         });
 *
 */
public class FaceGame {

    // The image map with the clickable areas
    private ImageView imageAreasView;

    // To be used for delaying posts
    private Handler handler;

    /**
     * Sets the game off by initiating default values.
     *
     * @param imageAreasView - Image map with the clickable areas
     */
    public FaceGame(ImageView imageAreasView) {
        this.imageAreasView = imageAreasView;

        handler = new Handler();
    }

    /**
     * Identify where the user has touched and play the
     * corresponding body part sound.
     *
     * @param view - The face image view
     * @param motionEvent - The action and position of the users touch
     */
    public void selectItem(View view, MotionEvent motionEvent) {

        // Store the users action and X/Y coordinates of the touch event
        final int action = motionEvent.getAction();
        final int eventX = (int) motionEvent.getX();
        final int eventY = (int) motionEvent.getY();

        // Context of the activity fetched from the view
        Context context = view.getContext();

        if ((action == MotionEvent.ACTION_UP) && GameUtils.isTouchEnabled()) {

            int touchColor = getSelectedPixelColor(eventX, eventY);

            playSelectedAreaSound(context, touchColor);

        }
    }

    /**
     * Initiate playback of the sound corresponding to the specified colors
     * in the mapped image.
     *
     * @param context - of the game
     * @param touchColor - user selected color
     */
    private void playSelectedAreaSound(Context context, int touchColor) {

        // Disable further touch events
        GameUtils.setTouchEnabled(false);

        if (closeMatch(Color.RED, touchColor)) SoundPlayback.play(context, R.raw.colors_red);
        if (closeMatch(Color.MAGENTA, touchColor)) SoundPlayback.play(context, R.raw.colors_white);
        if (closeMatch(Color.GREEN, touchColor)) SoundPlayback.play(context, R.raw.colors_green);
        if (closeMatch(Color.BLUE, touchColor)) SoundPlayback.play(context, R.raw.colors_blue);
        if (closeMatch(Color.BLACK, touchColor)) SoundPlayback.play(context, R.raw.colors_black);
        if (closeMatch(Color.YELLOW, touchColor)) SoundPlayback.play(context, R.raw.colors_yellow);
        if (closeMatch(Color.LTGRAY, touchColor)) SoundPlayback.play(context, R.raw.colors_grey);
        if (closeMatch(Color.GRAY, touchColor)) SoundPlayback.play(context, R.raw.colors_grey);
        if (closeMatch(Color.DKGRAY, touchColor)) SoundPlayback.play(context, R.raw.colors_grey);
        if (closeMatch(Color.CYAN, touchColor)) SoundPlayback.play(context, R.raw.colors_blue);

        // Enable touch events after sound playback
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                GameUtils.setTouchEnabled(true);

            }
        }, SoundPlayback.getSoundDuration());

    }

    /**
     * Create the color mapped image and detect the color of the
     * selected pixel.
     *
     * In simple terms:
     * - Bitmap is the frame and the colors
     * - Canvas is, well it's the canvas of the painting
     * - imageAreasView.draw(), basically draws ("paints")
     *   whatever is in the view on the canvas
     * - getPixel() returns the color of the selected pixel
     *   from the newly created canvas
     *
     * @param x - axis coordinates of the event
     * @param y - axis coordinates of the event
     * @return Color of the selected pixel
     */
    private int getSelectedPixelColor(int x, int y) {

        int pixelColor = 0;
        int width = imageAreasView.getMeasuredWidth();
        int height = imageAreasView.getMeasuredHeight();

        // Only run if imageAreasView has done a full layout,
        // in which case the measured width and height should be > 0.
        if (width > 0 && height > 0) {

            // Returns a mutable bitmap w/ the specified width and height.
            // The Bitmap.config describes how the pixels are stored, basically the quality of the colors.
            Bitmap imageAreasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            // The canvas hosts the draw cells, i.e. writes into the bitmap
            Canvas canvas = new Canvas(imageAreasBitmap);

            // Manually render this view to the given Canvas
            imageAreasView.draw(canvas);

            // Store the color of the selected pixel
            pixelColor = imageAreasBitmap.getPixel(x, y);

            imageAreasBitmap.recycle();
        }

        return pixelColor;
    }

    /**
     * Check if the selected color matches the given color,
     * within the specified tolerance level. This is done by comparing
     * three channels R, G and B, which range from 0-255 in value.
     *
     * So if all three values are a close match, then their subtraction with each other
     * should be less than 25. Which means that the selected color and the given
     * color are most likely the same color.
     *
     * @param givenColor - Pre-determined color
     * @param touchColor - User selected color
     * @return true only if all the comparisons are true
     */
    private boolean closeMatch (int givenColor, int touchColor) {

        int tolerance = 25;

        // Compare the three channels
        if (Math.abs (Color.red (givenColor) - Color.red (touchColor)) > tolerance ) return false;
        if (Math.abs (Color.green (givenColor) - Color.green (touchColor)) > tolerance ) return false;
        if (Math.abs (Color.blue (givenColor) - Color.blue (touchColor)) > tolerance ) return false;

        return true;
    }
}
